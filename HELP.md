# Simple Inventory API

### Reference Documentation
This is a String Boot RESTful API which is intended for storing and retrieving
of inventory items from different manufacturers.
Script ```data.sql``` provided in ```resources``` folder creates initial values in the 
in-memory H2 database.

### Prerequisites
When running this API in Intellij IDEA, please ensure that you have Lombok plugin
installed (```Settings``` -> ```Plugins```, search for "Lombok") and you have annotation
processing enabled (```Settings``` -> ```Build,Execution, Deployment``` -> ```Compiler``` ->
```Annotation Processors```, check ```Enable Annotation processing```).

HTTP Basic security must be used with credentials username ```username``` and password 
```password```, they are currently included in ```application.yml```, with password being BCrypt 
encoded to avoid its explicit exposure. So when testing in Postman, please ensure you select
```Basic Auth``` in Authorization section of your request and enter these credentials. 

###Usage
This Spring Boot app, when deployed locally, exposes one endpoint:
```GET http://localhost:8080/inventory?skip=offset_value&limit=limit_value```

This API endpoint should respond with an array of inventory items starting
with position ```offset_value + 1```, length of array is ```limit_value```.

```GET http://localhost:8080/inventory/item_id```

This API endpoint should respond with a specific inventory item with ID ```item_id``` in a DB.

```POST http://localhost:8080/inventory```

This API endpoint creates a new inventory item in the system, provided that no item with the same name
already exists in the inventory and manufacturer specified in it is valid (identified by its ID or name).

