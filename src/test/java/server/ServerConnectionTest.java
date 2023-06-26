package server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ServerConnectionTest {

    @Mock
    private Socket socket;

    @Mock
    private BufferedReader reader;

    @Mock
    private BufferedWriter writer;

    @InjectMocks
    @Spy
    private ServerConnection connection;

    @ParameterizedTest
    @ValueSource(strings = {"hello", "how are you", "what is your name?"})
    void userIsExit_typical_word(String word) throws IOException {
        assertFalse(connection.userIsExit(word));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"exit"})
    void userIsExit_when_came_null_or_exit(String word) throws IOException {
        doNothing().when(connection).closeService();

        assertTrue(connection.userIsExit(word));
    }

    @ParameterizedTest
    @ValueSource(strings = {"hello", "how are you", "what is your name?"})
    void send(String word) throws IOException {

        connection.send(word);

        verify(writer).write(word + "\n");
        verify(writer).flush();
    }

    @Test
    void send_when_throws_exception() throws IOException {
        doThrow(new IOException("something went wrong")).when(writer).write(anyString());

        String testMessage = "test message";
        Assertions.assertThrows(IOException.class, () -> connection.send(testMessage));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void closeService(boolean isClosed) throws IOException {
        doReturn(isClosed).when(socket).isClosed();

        connection.closeService();

        if (!isClosed) {
            verify(socket).close();
        }
    }
}