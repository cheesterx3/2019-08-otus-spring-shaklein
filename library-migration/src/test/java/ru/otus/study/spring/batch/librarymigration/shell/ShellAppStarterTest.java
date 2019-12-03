package ru.otus.study.spring.batch.librarymigration.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.study.spring.batch.librarymigration.service.JobManageService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = {
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false",
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"
})
@ComponentScan(basePackages = {
        "ru.otus.study.spring.batch.librarymigration.domain.jdbc",
        "ru.otus.study.spring.batch.librarymigration.config",
        "ru.otus.study.spring.batch.librarymigration.component"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("Сервис приёма команд через shell")
class ShellAppStarterTest {
    @Autowired
    private Shell shell;
    @MockBean
    private JobManageService jobManageService;


    @Test
    @DisplayName("должен запускать job при вводе команды 'start' через сервис JobManageService, если сервис готов к запуску работы ")
    void correctStartIfServiceIsReady() throws Exception {
        given(jobManageService.isAvailableToStart()).willReturn(true);
        shell.evaluate(() -> "start");
        verify(jobManageService, times(1)).startJob();
    }

    @Test
    @DisplayName("должен выдавать ошибку при вводе команды 'start', если сервис JobManageService не готов к запуску работы ")
    void errorOnStartIfServiceIsNoReady() {
        given(jobManageService.isAvailableToStart()).willReturn(false);
        final Object evaluate = shell.evaluate(() -> "start");
        assertThat(evaluate).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

    @Test
    @DisplayName("должен перезапускать job при вводе команды 'restart' через сервис JobManageService, если сервис готов к перезапуску работы ")
    void correctRestartIfServiceIsReadyToRestart() throws Exception{
        given(jobManageService.isAvailableToReStart()).willReturn(true);
        shell.evaluate(() -> "restart");
        verify(jobManageService, times(1)).restartJob();
    }

    @Test
    @DisplayName("должен выдавать ошибку при вводе команды 'restart', если сервис JobManageService не готов к перезапуску работы ")
    void errorOnRestartIfServiceIsNotReadyToRestart() throws Exception{
        given(jobManageService.isAvailableToReStart()).willReturn(false);
        final Object evaluate = shell.evaluate(() -> "restart");
        assertThat(evaluate).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }
}