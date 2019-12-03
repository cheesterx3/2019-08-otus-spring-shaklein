package ru.otus.study.spring.librarymvc.security;

import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.model.*;

import java.util.List;

/**
 * Переработанный {@link DefaultPermissionGrantingStrategy}. По определённым причинам с точки зрения оптимизации
 * при работе с БД через JDBC в дефолтном обработчике проверка идёт по полному совпадению маски разрешений,
 * а не по включению разрешений. Т.к. в Монге подобных проблем с производиельностью запросов возникнуть не должно,
 * то перерабоатываем с учётом именно включения разрешения в маску
 */
public class CustomPermissionGrantingStrategy implements PermissionGrantingStrategy {

    CustomPermissionGrantingStrategy() {
    }

    @Override
    public boolean isGranted(Acl acl, List<Permission> permissions, List<Sid> sids, boolean administrativeMode) {
        final List<AccessControlEntry> aces = acl.getEntries();

        AccessControlEntry firstRejection = null;

        for (Permission p : permissions) {
            for (Sid sid : sids) {
                // Attempt to find exact match for this permission mask and SID
                boolean scanNextSid = true;
                for (AccessControlEntry ace : aces) {
                    // Собственно тут переработана проверка
                    // Заменено ace.getPermission().getMask() == p.getMask()
                    //
                    if (containsPermission(p, ace) && ace.getSid().equals(sid)) {
                        // Found a matching ACE, so its authorization decision will
                        // prevail
                        if (ace.isGranting()) {
                            return true;
                        }
                        // Failure for this permission, so stop search
                        // We will see if they have a different permission
                        // (this permission is 100% rejected for this SID)
                        if (firstRejection == null) {
                            // Store first rejection for auditing reasons
                            firstRejection = ace;
                        }

                        scanNextSid = false; // helps break the loop

                        break; // exit aces loop
                    }
                }

                if (!scanNextSid) {
                    break; // exit SID for loop (now try next permission)
                }
            }
        }

        if (firstRejection != null) {
            return false;
        }

        // No matches have been found so far
        if (acl.isEntriesInheriting() && (acl.getParentAcl() != null)) {
            // We have a parent, so let them try to find a matching ACE
            return acl.getParentAcl().isGranted(permissions, sids, false);
        } else {
            // We either have no parent, or we're the uppermost parent
            throw new NotFoundException(
                    "Unable to locate a matching ACE for passed permissions and SIDs");
        }
    }

    private boolean containsPermission(Permission p, AccessControlEntry ace) {
        return (ace.getPermission().getMask() & p.getMask()) == p.getMask();
    }
}

