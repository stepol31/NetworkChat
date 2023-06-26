package logger;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
    private static Logger log;

    public static Logger getInstance() {
        if (log == null) {
            log = new Logger();
        }
        return log;
    }

    private Logger() {
    }

    public void logging(String msg) throws IOException {
        try (FileWriter fileWriter = new FileWriter("file.log", true)) {
            fileWriter.write(new SimpleDateFormat("dd.MM.yyyy HH.mm.ss ").format(Calendar.getInstance().getTime()) + " " + msg + '\n');
            fileWriter.flush();
        }
    }
}
