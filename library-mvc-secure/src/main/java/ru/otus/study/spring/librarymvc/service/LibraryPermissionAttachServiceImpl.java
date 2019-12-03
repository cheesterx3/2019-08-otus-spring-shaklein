package ru.otus.study.spring.librarymvc.service;

import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.study.spring.librarymvc.domain.Book;

@Service
public class LibraryPermissionAttachServiceImpl implements LibraryPermissionAttachService {
    private final MutableAclService aclService;
    private final Sid admin, librarian, users, anon;
    private final Permission fullPermission;

    public LibraryPermissionAttachServiceImpl(MutableAclService aclService) {
        this.aclService = aclService;
        admin = new GrantedAuthoritySid("ROLE_ADMIN");
        librarian = new GrantedAuthoritySid("ROLE_LIBRARIAN");
        users = new GrantedAuthoritySid("ROLE_USER");
        anon = new GrantedAuthoritySid("ROLE_ANONYMOUS");
        fullPermission = new CumulativePermission()
                .set(BasePermission.READ)
                .set(BasePermission.ADMINISTRATION);
    }

    @Override
    @Transactional
    public Book addDefaultPermissionsForBook(Book book) {
        final ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(Book.class, book.getId());
        final PrincipalSid owner = new PrincipalSid(SecurityContextHolder.getContext().getAuthentication());

        final MutableAcl acl = aclService.createAcl(objectIdentity);
        acl.setOwner(owner);
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, owner, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, users, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, anon, true);
        acl.insertAce(acl.getEntries().size(), fullPermission, admin, true);
        acl.insertAce(acl.getEntries().size(), fullPermission, librarian, true);
        aclService.updateAcl(acl);
        return book;
    }
}
