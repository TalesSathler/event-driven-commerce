export const environment = {
  production: true,
  api: {
    productService: '/api',
    inventoryService: '/api',
    authService: '/api',
  },
  swagger: {
    productService: 'http://product-service:8082/swagger-ui/index.html',
    inventoryService: 'http://inventory-service:8083/swagger-ui/index.html',
    authService: 'http://auth-service:8081/swagger-ui/index.html',
  },
};
