package com.shades.services.fragx;

import Entities.InventoryEntity;
import com.shades.controller.InventoryController;
import com.shades.dao.InventoryDao;
import com.shades.services.fragx.Exceptions.BadAccessIdOrKeyException;
import com.shades.services.fragx.Exceptions.BadItemIdException;
import com.shades.services.fragx.Exceptions.HttpErrorException;
import com.shades.services.fragx.Models.Product;
import com.shades.utilities.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Transactional
@Service
public class FragxService {

    private String apiId;
    private String apiKey;

    private static final Logger logger = Logger.getLogger(InventoryController.class);

    @Autowired
    private InventoryDao inventoryDao;

    public void updateInventory() {

        Set<InventoryEntity> inventorySet = new HashSet<>();

        try {
            FrgxListingApiClient client = new FrgxListingApiClient(apiId, apiKey);
            List<Product> products = client.getAllProductList();

            //TODO: FIX THIS LAMBDA EXPRESSION, USE FUNCTIONAL - COLLECT
            products.stream().forEach(s -> {
                InventoryEntity inventoryEntity = new InventoryEntity();
                inventoryEntity.setSku(s.getItemId());
                inventoryEntity.setSupplierId(501);
                inventoryEntity.setQuantity(s.isInstock() ? 10 : 0);
                inventoryEntity.setSupplierProductId(s.getParentCode());
                inventoryEntity.setSupplierPrice(s.getWholesalePriceUSD());
                inventoryEntity.setShadesSellingPrice(Utils.shadesPrices(s.getWholesalePriceUSD()));
                inventoryEntity.setShippingCost(5.00);
                inventoryEntity.setLastUpdate(new Timestamp(System.currentTimeMillis()));
                inventorySet.add(inventoryEntity);
            });

        } catch (Exception e) {
            //TODO: CATCH THIS EXCEPTION BETTER
            e.printStackTrace();
        }
        inventoryDao.updateInventory(inventorySet);
    }



    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Product getProductDetails(InventoryEntity product) {

        Product productInfo = null;
        try {
            FrgxListingApiClient client = new FrgxListingApiClient(apiId, apiKey);
            productInfo = client.getProductById(product.getSku());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadAccessIdOrKeyException e) {
            e.printStackTrace();
        } catch (HttpErrorException e) {
            e.printStackTrace();
        } catch (BadItemIdException e) {
            e.printStackTrace();
        }

        return productInfo;
    }
}