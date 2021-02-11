package me.pedrocaires.chapt.core.message;

import me.pedrocaires.chapt.core.constants.ParameterConstant;
import me.pedrocaires.chapt.core.exception.InvalidInputException;
import me.pedrocaires.chapt.core.testinterface.NoParameterConstructorTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

import static me.pedrocaires.chapt.core.constants.GeneralConstant.USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest implements NoParameterConstructorTest {

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    HttpServletResponse httpServletResponse;

    @Mock
    List<LastMessageResponse> lastMessageResponses;

    @Mock
    MessageService messageService;

    MessageController messageController;

    @BeforeEach
    void init() {
        this.messageController = new MessageController(messageService);
    }

    @Test
    void shouldThrowWhenSelectLastMessageInputIsInvalid() {
        var userId = 1;
        var invalidNumberToParse = "";
        when(httpServletRequest.getAttribute(USER_ID)).thenReturn(userId);
        when(httpServletRequest.getParameter(ParameterConstant.PAGE_SIZE)).thenReturn(invalidNumberToParse);
        when(httpServletRequest.getParameter(ParameterConstant.PAGE_INDEX)).thenReturn(invalidNumberToParse);
        when(httpServletRequest.getParameter(ParameterConstant.TO)).thenReturn(invalidNumberToParse);

        Assertions.assertThrows(InvalidInputException.class, () -> messageController.doCustomGet(httpServletRequest, httpServletResponse));
    }

    @Test
    void shouldSelectLastMessages() throws SQLException {
        var userId = 1;
        var pageSize = 1;
        var pageIndex = 1;
        var to = 1;
        when(httpServletRequest.getAttribute(USER_ID)).thenReturn(userId);
        when(httpServletRequest.getParameter(ParameterConstant.PAGE_SIZE)).thenReturn(String.valueOf(pageSize));
        when(httpServletRequest.getParameter(ParameterConstant.PAGE_INDEX)).thenReturn(String.valueOf(pageIndex));
        when(httpServletRequest.getParameter(ParameterConstant.TO)).thenReturn(String.valueOf(to));
        when(messageService.selectLastMessages(argThat(lastMessageRequest -> lastMessageRequest.getFrom().equals(userId)
                && lastMessageRequest.getPageSize() == pageSize
                && lastMessageRequest.getPageIndex() == pageIndex
                && lastMessageRequest.getTo().equals(to)))).thenReturn(lastMessageResponses);

        var selectedLastMessages = messageController.doCustomGet(httpServletRequest, httpServletResponse);

        assertEquals(lastMessageResponses, selectedLastMessages);
    }

    @Test
    @Override
    public void shouldInstantiateClassWithNoParameters() {
        new MessageController();
    }
}
