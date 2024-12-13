-- 테스트데이터
-- token.secret="TokenSecretTokenSecretTokenSecretTokenSecretTokenSecret", admin.password="test" 기준

-- 노숙인 서비스 약관
INSERT INTO wildflower_gardening.homeless_terms (deprecated_date, is_essential, start_date, created_at, id, title,
                                                 detail)
VALUES (null, true, '2024-06-01', '2024-06-04 22:34:49.497696', 1, '위치정보 수집 동의',
        'blablablablablablablablablablablablablablablablablablablablablablablabla');

-- 센터 계정
INSERT INTO wildflower_gardening.shelter (latitude, longitude, created_at, id, last_updated_at, name,
                                          phone_number, admin_id)
VALUES (90.00000000, 180.00000000, '2024-05-24 13:00:39.553443', 1, '2024-05-24 13:00:39.553443', '테스트 센터명 1',
        '01012341234', 1);

-- 슈퍼 관리자 계정
INSERT INTO wildflower_gardening.shelter_account (phone_number, id, shelter_id, email, password, name, user_role,
                                                  created_at, last_updated_at)
VALUES ('01012341234', 1, 1, 'email@email.com',
        '{bcrypt}$2a$10$GIfRKyTHl1Qv4b7fklKj2OFiPN90orA16pqgRHvZ6jR6vpSg3Zghq',
        '슈퍼맨', 'SHELTER_ADMIN', '2024-05-24 13:00:39.553443', '2024-05-24 13:00:39.553443');
-- 센터 핀 번호
INSERT
INTO shelter_pin (pin, created_at, last_updated_at, shelter_id)
VALUES ('1234', NOW(), NOW(), 1);

-- 노숙인 계정
INSERT INTO wildflower_gardening.homeless (admission_date, birth_date, created_at, id, last_updated_at, shelter_id,
                                           device_id, name, phone_number, room)
VALUES ('2024-08-01', '1970-05-15', '2024-05-24 13:05:48.973751', 1, '2024-05-24 13:06:05.323296', 1,
        'test_device_id_1', '홍길동', '01000000000', 'A동 101호');
INSERT INTO wildflower_gardening.homeless (admission_date, birth_date, created_at, id, last_updated_at, shelter_id,
                                           device_id, name, phone_number, room)
VALUES ('2024-05-24', '1970-05-15', '2024-05-24 13:06:36.398729', 2, '2024-05-24 13:07:13.145479', 1,
        'test_device_id_2', '임꺽정', '01000000001', 'A동 101호');
INSERT INTO wildflower_gardening.homeless (admission_date, birth_date, created_at, id, last_updated_at, shelter_id,
                                           device_id, name, phone_number, room)
VALUES ('2024-05-24', '1970-05-15', '2024-05-24 13:08:12.266395', 3, '2024-05-24 13:08:12.266395', 1,
        'test_device_id_3', '김철수', '01000000002', 'A동 101호');
INSERT INTO wildflower_gardening.homeless (admission_date, birth_date, created_at, id, last_updated_at, shelter_id,
                                           device_id, name, phone_number, room)
VALUES ('2024-05-24', '1970-05-15', '2024-05-24 13:09:23.458885', 4, '2024-05-24 13:09:36.490067', 1,
        'test_device_id_4', '이철수', '01000000003', 'B-101');
INSERT INTO wildflower_gardening.homeless (admission_date, birth_date, created_at, id, last_updated_at, shelter_id,
                                           device_id, name, phone_number, room)
VALUES ('2024-05-24', '1970-05-15', '2024-05-24 13:10:13.531983', 5, '2024-05-24 13:10:13.531983', 1,
        'test_device_id_5', '박민수', '01000000004', 'B 101');
INSERT INTO wildflower_gardening.homeless (admission_date, birth_date, created_at, id, last_updated_at, shelter_id,
                                           device_id, name, phone_number, room)
VALUES (null, null, '2024-05-24 13:11:34.082521', 6, '2024-05-24 13:11:48.032914', 1, 'test_device_id_6', '최민수', null,
        'B동 101호');
INSERT INTO wildflower_gardening.homeless (admission_date, birth_date, created_at, id, last_updated_at, shelter_id,
                                           device_id, name, phone_number, room)
VALUES (null, null, '2024-05-24 13:12:01.155215', 7, '2024-05-24 13:12:15.130980', 1, 'test_device_id_7', '유민수', null,
        'C동 202호');

-- 외박 신청 장부
INSERT INTO wildflower_gardening.sleepover (creator_type, end_date, start_date, created_at, deleted_at, homeless_id, id,
                                            shelter_id, emergency_contact, homeless_name, homeless_phone_number, reason)
VALUES (2, '2024-11-13', '2024-11-10', '2024-06-16 23:51:42.874313', null, 1, 1, 1, 'string', '홍길동', '01000000000',
        'string');
INSERT INTO wildflower_gardening.sleepover (creator_type, end_date, start_date, created_at, deleted_at, homeless_id, id,
                                            shelter_id, emergency_contact, homeless_name, homeless_phone_number, reason)
VALUES (2, '2024-06-18', '2024-06-17', '2024-06-16 23:51:47.759026', '2024-06-16 23:52:21.186527', 1, 2, 1, 'string',
        '홍길동', '01000000000', 'string');
INSERT INTO wildflower_gardening.sleepover (creator_type, end_date, start_date, created_at, deleted_at, homeless_id, id,
                                            shelter_id, emergency_contact, homeless_name, homeless_phone_number, reason)
VALUES (2, '2024-06-19', '2024-06-18', '2024-06-16 23:51:50.945526', null, 1, 3, 1, 'string', '홍길동', '01000000000',
        'string');
INSERT INTO wildflower_gardening.sleepover (creator_type, end_date, start_date, created_at, deleted_at, homeless_id, id,
                                            shelter_id, emergency_contact, homeless_name, homeless_phone_number, reason)
VALUES (2, '2024-06-30', '2024-06-15', '2024-06-16 23:56:16.316122', null, 2, 4, 1, 'string', '임꺽정', '01000000001',
        'string');


-- 위치 기록
INSERT INTO wildflower_gardening.location_tracking
(created_at, homeless_id, last_updated_at, shelter_id, tracked_at, location_status)
VALUES ('2024-09-25 13:49:29.250967', 1, '2024-09-25 13:49:29.250967', 1, '2024-09-25 13:49:29.250967', 'OUT_SHELTER');
INSERT INTO wildflower_gardening.location_tracking
(created_at, homeless_id, last_updated_at, shelter_id, tracked_at, location_status)
VALUES ('2024-09-25 13:49:29.250967', 2, '2024-09-25 13:49:29.250967', 1, '2024-09-25 13:49:29.250967', 'OUT_SHELTER');
INSERT INTO wildflower_gardening.location_tracking
(created_at, homeless_id, last_updated_at, shelter_id, tracked_at, location_status)
VALUES ('2024-09-25 13:49:29.250967', 3, '2024-09-25 13:49:29.250967', 1, '2024-09-25 13:49:29.250967', 'IN_SHELTER');
INSERT INTO wildflower_gardening.location_tracking
(created_at, homeless_id, last_updated_at, shelter_id, tracked_at, location_status)
VALUES ('2024-09-25 13:49:29.250967', 4, '2024-09-25 13:49:29.250967', 1, '2024-09-25 13:49:29.250967', 'OUT_SHELTER');
INSERT INTO wildflower_gardening.location_tracking
(created_at, homeless_id, last_updated_at, shelter_id, tracked_at, location_status)
VALUES ('2024-09-25 13:49:29.250967', 5, '2024-09-25 13:49:29.250967', 1, '2024-09-25 13:49:29.250967', 'OUT_SHELTER');
INSERT INTO wildflower_gardening.location_tracking
(created_at, homeless_id, last_updated_at, shelter_id, tracked_at, location_status)
VALUES ('2024-09-25 13:49:29.250967', 6, '2024-09-25 13:49:29.250967', 1, '2024-09-25 13:49:29.250967', 'OUT_SHELTER');
INSERT INTO wildflower_gardening.location_tracking
(created_at, homeless_id, last_updated_at, shelter_id, tracked_at, location_status)
VALUES ('2024-09-25 13:49:29.250967', 7, '2024-09-25 13:49:29.250967', 1, '2024-09-25 13:49:29.250967', 'OUT_SHELTER');

