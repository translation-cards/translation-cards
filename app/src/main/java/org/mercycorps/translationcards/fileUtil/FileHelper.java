package org.mercycorps.translationcards.fileUtil;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileHelper {

    public  FileDescriptor createFileDescriptor(String fileName) throws IOException {
        return new FileInputStream(fileName).getFD();
    }
}
