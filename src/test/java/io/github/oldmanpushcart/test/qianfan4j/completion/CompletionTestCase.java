package io.github.oldmanpushcart.test.qianfan4j.completion;

import io.github.oldmanpushcart.qianfan4j.completion.CompletionModel;
import io.github.oldmanpushcart.qianfan4j.completion.CompletionOptions;
import io.github.oldmanpushcart.qianfan4j.completion.CompletionRequest;
import io.github.oldmanpushcart.test.qianfan4j.LoadingEnv;
import io.github.oldmanpushcart.test.qianfan4j.QianFanAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompletionTestCase implements LoadingEnv {

    @Test
    public void test$completion() {

        final var request = CompletionRequest.newBuilder()
                .model(CompletionModel.SQL_CODER_7B)
                .prompt("""
                        ### Task
                        Generate a SQL query to answer the following question:
                        `What is our total revenue by product in the last week?`
                                                
                        ### Database Schema
                        This query will run on a database whose schema is represented in this string:
                                                
                        CREATE TABLE products (
                          product_id INTEGER PRIMARY KEY, -- Unique ID for each product
                          name VARCHAR(50),               -- Name of the product
                          price DECIMAL(10,2),            -- Price of each unit of the product
                          quantity INTEGER                -- Current quantity in stock
                        );
                                                
                        CREATE TABLE customers (
                          customer_id INTEGER PRIMARY KEY,  -- Unique ID for each customer
                          name VARCHAR(50),                 -- Name of the customer
                          address VARCHAR(100)              -- Mailing address of the customer
                        );
                                                
                        CREATE TABLE salespeople (
                          salesperson_id INTEGER PRIMARY KEY, -- Unique ID for each salesperson
                          name VARCHAR(50),                   -- Name of the salesperson
                          region VARCHAR(50)                  -- Geographic sales region
                        );
                                                
                        CREATE TABLE sales (
                          sale_id INTEGER PRIMARY KEY,  -- Unique ID for each sale
                          product_id INTEGER,           -- ID of product sold
                          customer_id INTEGER,          -- ID of customer who made purchase
                          salesperson_id INTEGER,       -- ID of salesperson who made the sale
                          sale_date DATE,               -- Date the sale occurred
                          quantity INTEGER              -- Quantity of product sold
                        );
                                                
                        CREATE TABLE product_suppliers (
                          supplier_id INTEGER PRIMARY KEY,  -- Unique ID for each supplier
                          product_id INTEGER,               -- Product ID supplied
                          supply_price DECIMAL(10,2)        -- Unit price charged by supplier
                        );
                                                
                        -- sales.product_id can be joined with products.product_id
                        -- sales.customer_id can be joined with customers.customer_id
                        -- sales.salesperson_id can be joined with salespeople.salesperson_id
                        -- product_suppliers.product_id can be joined with products.product_id
                                                
                        ### SQL
                        Given the database schema, here is the SQL query that answers `What is our total revenue by product in the last week?`:
                        """)
                .option(CompletionOptions.IS_STREAM, true)
                .build();

        final var response = client.completion(request)
                .async()
                .join();

        Assertions.assertTrue(response.isLast());
        Assertions.assertTrue(response.isSafe());
        Assertions.assertTrue(response.content().contains("SELECT"));
        QianFanAssertions.assertAlgoResponse(response);

    }

}
