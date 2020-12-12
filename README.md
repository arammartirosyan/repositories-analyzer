# GitHub API repositories analyzer

The project is using GitHub public API for retrieving repositories/commits/contributors etc.

- Search repositories
- Retrieve specific repository contributors
- Retrieve specific repository commits

# How to run 

To run the application you can build/run using maven or you can use docker-compose with the latest docker images already pushed to dockerhub.
```sh
$ docker-compose up
```

After running just check the swagger ui with openapi documentation.
[API docs](http://localhost:8081/api/swagger-ui/index.html?configUrl=/api/v3/api-docs/swagger-config)