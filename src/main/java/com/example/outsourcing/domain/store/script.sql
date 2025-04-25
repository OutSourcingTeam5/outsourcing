use outsourcing;



SELECT r.id, r.content, r.rating, r.is_deleted, o.store_id
FROM reviews r
         JOIN orders o ON r.order_id = o.id
WHERE o.store_id = ?
  AND (r.is_deleted = false OR r.is_deleted IS NULL)
ORDER BY r.created_at DESC;
