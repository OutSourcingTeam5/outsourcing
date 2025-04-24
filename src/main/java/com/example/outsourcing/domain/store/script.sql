use outsourcing;

INSERT INTO users (created_at,
                   updated_at,
                   deleted_at,
                   email,
                   is_deleted,
                   name,
                   password,
                   provider,
                   provider_id,
                   role)
VALUES (NOW(), -- created_at
        NULL, -- updated_at
        NULL, -- deleted_at
        'owner@example.com',
        FALSE, -- is_deleted
        'Owner Name',
        'owner_password', -- 실제 운영 시엔 해싱 필요
        'kakao',
        'kakao_owner_001',
        'OWNER');

INSERT INTO users (created_at,
                   updated_at,
                   deleted_at,
                   email,
                   is_deleted,
                   name,
                   password,
                   provider,
                   provider_id,
                   role)
VALUES (NOW(),
        NULL,
        NULL,
        'user@example.com',
        FALSE,
        'User Name',
        'user_password',
        'kakao',
        'kakao_user_001',
        'USER');


INSERT INTO menu (name, description, price, status, store_id)
VALUES ('치킨마요덮밥', '담백한 치킨마요 덮밥', 6500, 'ACTIVE', 5);

INSERT INTO menu (name, description, price, status, store_id)
VALUES ('돈까스정식', '바삭한 돈까스와 밥, 국 포함', 8500, 'ACTIVE', 5);

INSERT INTO menu (name, description, price, status, store_id)
VALUES ('오므라이스', '케첩소스 듬뿍 오므라이스', 7000, 'ACTIVE', 5);

INSERT INTO menu (name, description, price, status, store_id)
VALUES ('떡볶이', '매콤달콤 국물떡볶이', 5000, 'ACTIVE', 5);