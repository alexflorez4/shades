package com.shades.client;

import com.shades.services.az.AzProcess;
import com.shades.services.misc.AppServices;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.List;
import java.util.Set;

public class client {

    static Logger logger = Logger.getLogger(client.class);

    public static void main(String[] args) {

        logger.info("In main class.");
        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("/application.xml");

        /*try {
            AzProcess azProcess = container.getBean(AzProcess.class);
            File azInventory = new File("C:\\Users\\alexf\\Documents\\Development\\Projects\\Shades\\azInventory.xlsx");
            azProcess.updateInventory(azInventory);

        }catch (Exception e){
            System.out.println("Exception: \n\n");
            System.out.println(e);
        }*/

        AppServices appServices = container.getBean(AppServices.class);
        List<String> products = appServices.allProductsSet();
        System.out.println(products.size());
    }
}
