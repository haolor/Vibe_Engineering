# Skill: Database Design

## Concepts
Database design is the process of producing a detailed data model of a database. It determines how data is stored, related, and queried. Proper design prevents data anomalies, ensures integrity, and dictates long-term performance.

## Best Practices
- **Normalization (Up to 3NF):** Eliminate redundant data, but in MongoDB, denormalize (embed) data if it avoids slow cross-collection queries.
- **Foreign Keys:** MongoDB doesn't enforce foreign keys natively, handle logical links in the application layer.
- **Indexing:** Create indexes on fields used in `find()` or `aggregate()` pipelines.
- **Soft Deletes:** Instead of `DELETE`, use an `is_deleted` boolean or `deleted_at` timestamp for critical business data (users, orders) to allow recovery and audit trails.

## Workflow
1. **Entity Identification:** List the nouns in the User Story (e.g., User, Product, Order).
2. **Relationship Mapping:** Define 1:1, 1:N, or N:M relationships (use junction tables for N:M).
3. **Draft Schema:** Write the `@Document` classes for Spring Data MongoDB.
4. **Review Constraints:** Add `@Indexed(unique = true)` where necessary.
5. **Generate Migration:** Use tools like Mongock to handle schema migrations if necessary.

## Examples
*Designing a Transaction with Embedded Category (Denormalized)*
```java
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private Double amount;
    
    // Denormalized fields to avoid $lookup
    private String categoryName;
    private String categoryIcon;
    private String categoryColor;
}
```

## AI Usage Strategy
- Feed PM User Stories to the System Architect AI and ask for a MongoDB `@Document` schema.
- Ask AI: "Analyze this MongoDB aggregation pipeline and suggest missing indexes."

## Common Mistakes
- **Over-indexing:** Indexes speed up reads but slow down writes. Don't index every field.
- **Using UUIDs as Primary Keys in large tables without thought:** Random UUIDs cause index fragmentation in B-Trees. Use sequential IDs (ULID, Snowflake) or standard auto-increment integers unless distributed ID generation is required.

## Optimization Techniques
- **Denormalization (Carefully):** For read-heavy analytics dashboards, pre-calculating and storing a total (e.g., `total_sales` on a `User` table) is faster than running a massive `SUM()` query every time.
- **Materialized Views:** Use them in PostgreSQL to cache the results of complex joins and aggregations.
