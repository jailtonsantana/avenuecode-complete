---------------------------------------------------------------------------------------------------------
 A V E N U E   C O D E - C O D I N G   T E S T
---------------------------------------------------------------------------------------------------------
Author: Jailton da Silva Camargo Santana

-----------------------
Running the application
-----------------------
$ mvn spring-boot:run

------------------------------------
Running the suite of automated tests
------------------------------------
$ mvn test

-------------
Not delivered
-------------
- Update child product and images

---------------------------------------------------------------------------------------------------------
                                   A P I   D O C U M E N T A T I O N
---------------------------------------------------------------------------------------------------------
TITLE            | show all products without child products neither images
URL              | /products or /products?child=false&images=false
METHOD           | GET
URL PARAMS       | REQUIRED
                 |   None
                 | OPTIONAL
                 |  child=[boolean]
                 |  images=[boolean]
DATA PARAMS      |
SUCCESS RESPONSE | 200
ERROR RESPONSE   | 204
---------------------------------------------------------------------------------------------------------
TITLE            | show all products with child products
URL              | /products?child=true or /products?child=true&images=false
METHOD           | GET 
URL PARAMS       | REQUIRED
                 |  child=[boolean]
                 | OPTIONAL
                 |  images=[boolean]
DATA PARAMS      | 
SUCCESS RESPONSE | 200
ERROR RESPONSE   | 204
---------------------------------------------------------------------------------------------------------
TITLE            | show all products with images 
URL              | /products?images=true or /products?child=false&images=true
METHOD           | GET
URL PARAMS       | REQUIRED
                 |  images=[boolean]
                 | OPTIONAL
                 |  child=[boolean]
DATA PARAMS      |
SUCCESS RESPONSE | 200
ERROR RESPONSE   | 204
---------------------------------------------------------------------------------------------------------
TITLE            | show all products with child products and images
URL              | /products?child=true&images=true
METHOD           | GET
URL PARAMS       | REQUIRED
                 |  child=[boolean]
                 |  images=[boolean]
                 | OPTIONAL
                 |  None
DATA PARAMS      |
SUCCESS RESPONSE | 200
ERROR RESPONSE   | 204
---------------------------------------------------------------------------------------------------------
TITLE            | show a product without child products neither images
URL              | /products/<product id> or /products/<product id>?child=false&images=false
METHOD           | GET
URL PARAMS       | REQUIRED
                 |  None
                 | OPTIONAL
                 |  child=[boolean]
                 |  images=[boolean]
DATA PARAMS      |
SUCCESS RESPONSE | 200
ERROR RESPONSE   | 404
---------------------------------------------------------------------------------------------------------
TITLE            | show a product with child products
URL              | /products/<product id>?child=true or /products/<product id>?child=true&images=false
METHOD           | GET
URL PARAMS       | REQUIRED
                 |  child=[boolean]
                 | OPTIONAL
                 |  images=[boolean]
DATA PARAMS      |
SUCCESS RESPONSE | 200
ERROR RESPONSE   | 404
---------------------------------------------------------------------------------------------------------
TITLE            | show a product with images
URL              | /products/<product id>?images=true or /products/<product id>?child=false&images=true
METHOD           | GET
URL PARAMS       | REQUIRED
                 |  images=[boolean]
                 | OPTIONAL
                 |  child=[boolean]
DATA PARAMS      |
SUCCESS RESPONSE | 200
ERROR RESPONSE   | 404
---------------------------------------------------------------------------------------------------------
TITLE            | show a product with child products and images
URL              | /products/<product id>?child=true&images=true
METHOD           | GET
URL PARAMS       | REQUIRED
                 |  child=[boolean]
                 |  images=[boolean]
                 | OPTIONAL
                 |  None
DATA PARAMS      |
SUCCESS RESPONSE | 200
ERROR RESPONSE   | 404
---------------------------------------------------------------------------------------------------------
TITLE            | show child products for a product
URL              | /products/<product id>/products
METHOD           | GET
URL PARAMS       | REQUIRED
                 |   None
                 | OPTIONAL
                 |   None
DATA PARAMS      |
SUCCESS RESPONSE | 200
ERROR RESPONSE   | 204
---------------------------------------------------------------------------------------------------------
TITLE            | show images for a product
URL              | /products/<product id>/images
METHOD           | GET
URL PARAMS       | REQUIRED
                 |   None
                 | OPTIONAL
                 |   None
DATA PARAMS      |
SUCCESS RESPONSE | 200
ERROR RESPONSE   | 204
---------------------------------------------------------------------------------------------------------
TITLE            | insert a product, its child and images into the system
URL              | /products
METHOD           | POST
URL PARAMS       |
DATA PARAMS      | {
                 | "id" : [numeric],
                 | "name" : [string],
                 | "description": [string],
                 | "parentId": [numeric],
                 | "images":[image list],
                 | "child":[product list]
                 | }
                 | EXAMPLE
                 | {
                 | "id" : 0,
                 | "name" : "Coffee maker",
                 | "description":"Wallita Coffee maker",
                 | "parentId":0,
                 | "images":[],
                 | "child":[]
                 | }
SUCCESS RESPONSE | 201
ERROR RESPONSE   | 409
---------------------------------------------------------------------------------------------------------
TITLE            | insert an image
URL              | /products
METHOD           | POST
URL PARAMS       |
DATA PARAMS      | {
                 | "id" : [numeric],
                 | "type" : [string],
                 | "parentId": [numeric]
                 | }
                 | EXAMPLE
                 | {
                 | "id" : 0,
                 | "type" : "cover-front",
                 | "parentId": 4
                 | }
SUCCESS RESPONSE | 201
ERROR RESPONSE   | 409
---------------------------------------------------------------------------------------------------------
TITLE            | deleting a product, its child and images
URL              | /products/<product id>
METHOD           | DELETE
URL PARAMS       | REQUIRED
                 |   None
                 | OPTIONAL
                 |   None
DATA PARAMS      |
SUCCESS RESPONSE | 204
ERROR RESPONSE   | 404
---------------------------------------------------------------------------------------------------------
TITLE            | deleting a image
URL              | /images/<image id>
METHOD           | DELETE
URL PARAMS       | REQUIRED
                 |   None
                 | OPTIONAL
                 |   None
DATA PARAMS      |
SUCCESS RESPONSE | 204
ERROR RESPONSE   | 404
---------------------------------------------------------------------------------------------------------
TITLE            | update a product, its child and images into the system <NOT IMPLEMENTED>
URL              | /products/<product id>
METHOD           | PUT
URL PARAMS       |
DATA PARAMS      | {
                 | "id" : [numeric],
                 | "name" : [string],
                 | "description": [string],
                 | "parentId": [numeric],
                 | "images":[image list],
                 | "child":[product list]
                 | }
                 | EXAMPLE
                 | {
                 | "id" : 14,
                 | "name" : "Coffee maker",
                 | "description":"Wallita Coffee maker",
                 | "parentId":0,
                 | "images":[],
                 | "child":[]
                 | }
SUCCESS RESPONSE | 200
ERROR RESPONSE   | 404
---------------------------------------------------------------------------------------------------------
TITLE            | update an image <NOT IMPLEMENTED>
URL              | /images/<image id>
METHOD           | PUT
URL PARAMS       |
DATA PARAMS      | {
                 | "id" : [numeric],
                 | "type" : [string],
                 | "parentId": [numeric]
                 | }
                 | EXAMPLE
                 | {
                 | "id" : 7,
                 | "type" : "cover-front",
                 | "parentId": 4
                 | }
SUCCESS RESPONSE | 200
ERROR RESPONSE   | 404
---------------------------------------------------------------------------------------------------------



