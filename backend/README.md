```java
public class ProductPatchRequest {
 
    private Map<String, Object> fields;
 
    public Map<String, Object> getFields() {
        return fields;
    }
 
    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }
}

```

```java
// service/ProductService.java
@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // --- Approach A: Specific DTO update ---

    public Product updateBasicInfo(Long id, ProductBasicUpdateRequest request) {
        Product product = findOrThrow(id);
        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setActive(request.getActive());
        return productRepository.save(product);
    }

    public Product updateDetails(Long id, ProductDetailUpdateRequest request) {
        Product product = findOrThrow(id);

        // Only update non-null fields (partial update behavior)
        if (request.getDescription() != null)    product.setDescription(request.getDescription());
        if (request.getPrice() != null)          product.setPrice(request.getPrice());
        if (request.getCostPrice() != null)      product.setCostPrice(request.getCostPrice());
        if (request.getStockQuantity() != null)  product.setStockQuantity(request.getStockQuantity());
        if (request.getCategory() != null)       product.setCategory(request.getCategory());
        if (request.getBrand() != null)          product.setBrand(request.getBrand());
        if (request.getWeight() != null)         product.setWeight(request.getWeight());
        if (request.getWeightUnit() != null)     product.setWeightUnit(request.getWeightUnit());
        // ... repeat for all 40 fields

        return productRepository.save(product);
    }

    // --- Approach B: Generic Map-based patch (reflection) ---

    public Product patchProduct(Long id, Map<String, Object> fields) {
        Product product = findOrThrow(id);

        fields.forEach((fieldName, value) -> {
            try {
                Field field = Product.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                // Type coercion for common types
                Object coercedValue = coerceValue(field.getType(), value);
                field.set(product, coercedValue);

            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException("Unknown field: " + fieldName);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot update field: " + fieldName);
            }
        });

        return productRepository.save(product);
    }

    // Simple type coercion helper
    private Object coerceValue(Class<?> targetType, Object value) {
        if (value == null) return null;
        if (targetType.isInstance(value)) return value;

        String str = value.toString();
        if (targetType == BigDecimal.class)  return new BigDecimal(str);
        if (targetType == Integer.class)     return Integer.parseInt(str);
        if (targetType == Long.class)        return Long.parseLong(str);
        if (targetType == Double.class)      return Double.parseDouble(str);
        if (targetType == Boolean.class)     return Boolean.parseBoolean(str);
        if (targetType == LocalDate.class)   return LocalDate.parse(str);

        return value; // fallback, String etc.
    }

    private Product findOrThrow(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }
}

```