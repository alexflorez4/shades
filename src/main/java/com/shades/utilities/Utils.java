package com.shades.utilities;


import com.shades.exceptions.ShadesException;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class);

    public static File multipartToFile(MultipartFile multipart) throws ShadesException {

        try {
            File newFile = new File(multipart.getOriginalFilename());
            multipart.transferTo(newFile);
            return newFile;
        } catch (IOException e) {
            logger.info("Error at multipart file conversion. " + e.getLocalizedMessage());
            throw new ShadesException("Error at multipart file conversion to file. Message: " + e.getMessage());
        }

    }

    public static Float shadesPrices(Float cost){
        return new Float(cost + (cost * 0.15));
    }
}
