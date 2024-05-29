-- 테스트데이터
-- token.secret="TokenSecretTokenSecretTokenSecretTokenSecretTokenSecret", admin.password="test" 기준

-- 1. 센터 계정
INSERT INTO wildflower_gardening.shelter (latitude, longitude, created_at, id, last_updated_at, name, password) VALUES (90.00000000, 180.00000000, '2024-05-24 13:00:39.553443', 1, '2024-05-24 13:00:39.553443', '테스트 센터명 1', '{bcrypt}$2a$10$GIfRKyTHl1Qv4b7fklKj2OFiPN90orA16pqgRHvZ6jR6vpSg3Zghq');

-- 2. 노숙인 계정
INSERT INTO wildflower_gardening.homeless (admission_date, birth_date, created_at, id, last_location_tracked_at, last_updated_at, shelter_id, device_id, name, phone_number, room, last_location_status) VALUES ('2024-08-01', '1970-05-15', '2024-05-24 13:05:48.973751', 1, '2024-05-24 13:06:05.309729', '2024-05-24 13:06:05.323296', 1, 'test_device_id_1', '홍길동', '010-0000-0000', 'A동 101호', 'IN_SHELTER');
INSERT INTO wildflower_gardening.homeless (admission_date, birth_date, created_at, id, last_location_tracked_at, last_updated_at, shelter_id, device_id, name, phone_number, room, last_location_status) VALUES ('2024-05-24', '1970-05-15', '2024-05-24 13:06:36.398729', 2, '2024-05-24 13:07:13.135502', '2024-05-24 13:07:13.145479', 1, 'test_device_id_2', '임꺽정', '010-0000-0001', 'A동 101호', 'OUTING');
INSERT INTO wildflower_gardening.homeless (admission_date, birth_date, created_at, id, last_location_tracked_at, last_updated_at, shelter_id, device_id, name, phone_number, room, last_location_status) VALUES ('2024-05-24', '1970-05-15', '2024-05-24 13:08:12.266395', 3, null, '2024-05-24 13:08:12.266395', 1, 'test_device_id_3', '김철수', '010-0000-0002', 'A동 101호', null);
INSERT INTO wildflower_gardening.homeless (admission_date, birth_date, created_at, id, last_location_tracked_at, last_updated_at, shelter_id, device_id, name, phone_number, room, last_location_status) VALUES ('2024-05-24', '1970-05-15', '2024-05-24 13:09:23.458885', 4, '2024-05-24 13:09:36.482588', '2024-05-24 13:09:36.490067', 1, 'test_device_id_4', '이철수', '010-0000-0003', 'B-101', 'IN_SHELTER');
INSERT INTO wildflower_gardening.homeless (admission_date, birth_date, created_at, id, last_location_tracked_at, last_updated_at, shelter_id, device_id, name, phone_number, room, last_location_status) VALUES ('2024-05-24', '1970-05-15', '2024-05-24 13:10:13.531983', 5, null, '2024-05-24 13:10:13.531983', 1, 'test_device_id_5', '박민수', '010-0000-0004', 'B 101', null);
INSERT INTO wildflower_gardening.homeless (admission_date, birth_date, created_at, id, last_location_tracked_at, last_updated_at, shelter_id, device_id, name, phone_number, room, last_location_status) VALUES (null, null, '2024-05-24 13:11:34.082521', 6, '2024-05-24 13:11:48.019050', '2024-05-24 13:11:48.032914', 1, 'test_device_id_6', '최민수', null, null, 'OUTING');
INSERT INTO wildflower_gardening.homeless (admission_date, birth_date, created_at, id, last_location_tracked_at, last_updated_at, shelter_id, device_id, name, phone_number, room, last_location_status) VALUES (null, null, '2024-05-24 13:12:01.155215', 7, '2024-05-24 13:12:15.118136', '2024-05-24 13:12:15.130980', 1, 'test_device_id_7', '유민수', null, null, 'IN_SHELTER');

-- 3. 외박 신청 장부
INSERT INTO wildflower_gardening.sleepover (creator_type, end_date, start_date, created_at, deleted_at, homeless_id, id, shelter_id, reason, emergency_contact) VALUES (2, '2024-09-01', '2024-05-24', '2024-05-24 13:07:02.277768', null, 2, 1, 1, '지인만남', '01012341234');
INSERT INTO wildflower_gardening.sleepover (creator_type, end_date, start_date, created_at, deleted_at, homeless_id, id, shelter_id, reason, emergency_contact) VALUES (2, '2024-09-01', '2024-05-24', '2024-05-24 13:09:43.303959', null, 4, 2, 1, '집에감', '02-129-129');
INSERT INTO wildflower_gardening.sleepover (creator_type, end_date, start_date, created_at, deleted_at, homeless_id, id, shelter_id, reason, emergency_contact) VALUES (2, '2024-05-25', '2024-05-24', '2024-05-24 13:10:36.950112', null, 5, 3, 1, '지인 만나ㅓ러', '');
