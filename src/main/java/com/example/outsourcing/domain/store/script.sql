use outsourcing;

INSERT INTO users (
    created_at,
    updated_at,
    deleted_at,
    email,
    is_deleted,
    name,
    password,
    provider,
    provider_id,
    role
) VALUES (
             NOW(),           -- created_at
             NULL,            -- updated_at
             NULL,            -- deleted_at
             'owner@example.com',
             FALSE,           -- is_deleted
             'Owner Name',
             'owner_password',  -- 실제 운영 시엔 해싱 필요
             'kakao',
             'kakao_owner_001',
             'OWNER'
         );

INSERT INTO users (
    created_at,
    updated_at,
    deleted_at,
    email,
    is_deleted,
    name,
    password,
    provider,
    provider_id,
    role
) VALUES (
             NOW(),
             NULL,
             NULL,
             'user@example.com',
             FALSE,
             'User Name',
             'user_password',
             'kakao',
             'kakao_user_001',
             'USER');