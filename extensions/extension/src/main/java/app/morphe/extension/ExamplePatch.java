package app.morphe.extension;

import java.io.*;
import org.joda.time.*;

@SuppressWarnings("unused")
public class ExamplePatch {

    public String DesugaringTest(String input) {
        if (input.isBlank())
            return null;

        return "Siema";
    }

    public DateTime getValidTillDateTime() {
        DateTime dateTime = DateTime.now();
        return dateTime;
    }
}
