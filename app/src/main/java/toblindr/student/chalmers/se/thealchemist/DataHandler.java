package toblindr.student.chalmers.se.thealchemist;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {
    private Context context;

    public DataHandler(Context context) {
        this.context = context;
    }

    public List<String> readRawTextFile(int resId, Resources resources) {
        InputStream inputStream = resources.openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        List<String> rows = new ArrayList<>();

        try {
            while ((line = buffreader.readLine()) != null) {
                if (!line.isEmpty()) {
                    rows.add(line);
                }
            }
        } catch (IOException e) {
            return null;
        }
        return rows;
    }

    public List<String> readPhoneFile(String filename) {
        List<String> listToReturn = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                listToReturn.add(line);
                System.out.println("Reading: " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return listToReturn;

    }

    public void writeToPhoneFile(String filename, String data) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
