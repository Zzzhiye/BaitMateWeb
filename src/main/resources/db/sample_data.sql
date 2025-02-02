-- Sample Admin Data
INSERT INTO baitmate.admin (name, username, email, address, password) VALUES
('John Admin', 'admin_john', 'admin@baitmate.com', '123 Admin Street, Singapore 123456', 'Admin123!');

-- Sample Users
INSERT INTO baitmate.app_user (username, password, phonenumber, email, age, gender, address, joindate, userstatus, profileimage) VALUES
('john_doe', 'Pass123!', '91234567', 'john@example.com', 25, 'Male', '123 Tampines St 45, Singapore 520123', '2024-01-01'::date, 'ACTIVE', NULL),
('jane_smith', 'Pass123!', '92345678', 'jane@example.com', 30, 'Female', '456 Bedok North Ave 3, Singapore 460456', '2024-01-02'::date, 'ACTIVE', NULL),
('mike_wilson', 'Pass123!', '93456789', 'mike@example.com', 35, 'Male', '789 Punggol Central, Singapore 820789', '2024-01-03'::date, 'ACTIVE', NULL),
('sarah_lee', 'Pass123!', '94567890', 'sarah@example.com', 28, 'Female', '321 Yishun Ring Road, Singapore 760321', '2024-01-04'::date, 'ACTIVE', NULL),
('david_tan', 'Pass123!', '95678901', 'david@example.com', 40, 'Male', '654 Jurong West St 91, Singapore 640654', '2024-01-05'::date, 'ACTIVE', NULL),
('lisa_wong', 'Pass123!', '96789012', 'lisa@example.com', 27, 'Female', '987 Woodlands Dr 50, Singapore 730987', '2024-01-06'::date, 'ACTIVE', NULL),
('peter_lim', 'Pass123!', '97890123', 'peter@example.com', 45, 'Male', '147 Ang Mo Kio Ave 3, Singapore 560147', '2024-01-07'::date, 'ACTIVE', NULL),
('mary_tan', 'Pass123!', '98901234', 'mary@example.com', 32, 'Female', '258 Serangoon Central, Singapore 550258', '2024-01-08'::date, 'ACTIVE', NULL),
('james_ng', 'Pass123!', '99012345', 'james@example.com', 38, 'Male', '369 Clementi Ave 2, Singapore 120369', '2024-01-09'::date, 'ACTIVE', NULL),
('emma_lau', 'Pass123!', '90123456', 'emma@example.com', 29, 'Female', '741 Pasir Ris Dr 3, Singapore 510741', '2024-01-10'::date, 'ACTIVE', NULL),
('william_teo', 'Pass123!', '91234567', 'william@example.com', 42, 'Male', '852 Hougang Ave 4, Singapore 530852', '2024-01-11'::date, 'ACTIVE', NULL),
('olivia_chen', 'Pass123!', '92345678', 'olivia@example.com', 31, 'Female', '963 Bukit Batok St 11, Singapore 650963', '2024-01-12'::date, 'ACTIVE', NULL),
('thomas_koh', 'Pass123!', '93456789', 'thomas@example.com', 36, 'Male', '159 Toa Payoh North, Singapore 310159', '2024-01-13'::date, 'ACTIVE', NULL);

-- Sample Fishing Locations
INSERT INTO baitmate.fishing_location (location_name, address, openinghours, latitude, longitude) VALUES
('Bedok Reservoir', 'Bedok Reservoir Road', '24/7', 1.3397, 103.9327),
('Lower Pierce Reservoir', 'Old Upper Thomson Road', '7:00-19:00', 1.3721, 103.8272),
('Punggol Waterway', 'Sentul Crescent', '24/7', 1.4041, 103.9076),
('Marina Reservoir', 'Marina Gardens Drive', '24/7', 1.2857, 103.8575),
('Jurong Lake', 'Yuan Ching Road', '24/7', 1.3397, 103.7272),
('Pasir Ris Park', '125 Elias Road', '24/7', 1.3721, 103.9493),
('East Coast Park', 'East Coast Park Service Road', '24/7', 1.3012, 103.9131);

-- Sample Fishing Spots with 3-7 random fish types
INSERT INTO baitmate.fishing_spots (name, latitude, longitude, environment, fish_types) VALUES
-- Bedok Reservoir Spots
('Bedok Reservoir Jetty A', 1.3398, 103.9328, 'Freshwater', 
  CASE (random() * 4)::int
    WHEN 0 THEN 'Tilapia, Catfish, Snakehead'
    WHEN 1 THEN 'Grass Carp, Silver Carp, Indian Carp, Bighead Carp'
    WHEN 2 THEN 'Black Spotted Barb, Silver Barb, Gourami, Climbing Perch, Mosquitofish'
    WHEN 3 THEN 'Pangasius, KnifeFish, Mudfish, Goby, Freshwater Eel, Gold Fish'
    ELSE 'Tilapia, Catfish, Snakehead, Grass Carp, Silver Carp, Indian Carp, Bighead Carp'
  END),
('Bedok Reservoir Fishing Deck', 1.3396, 103.9326, 'Freshwater',
  CASE (random() * 4)::int
    WHEN 0 THEN 'Tilapia, Catfish, Snakehead'
    WHEN 1 THEN 'Grass Carp, Silver Carp, Indian Carp, Bighead Carp'
    WHEN 2 THEN 'Black Spotted Barb, Silver Barb, Gourami, Climbing Perch, Mosquitofish'
    WHEN 3 THEN 'Pangasius, KnifeFish, Mudfish, Goby, Freshwater Eel, Gold Fish'
    ELSE 'Tilapia, Catfish, Snakehead, Grass Carp, Silver Carp, Indian Carp, Bighead Carp'
  END),

-- Lower Pierce Reservoir Spots
('Lower Pierce Boardwalk', 1.3722, 103.8273, 'Freshwater',
  CASE (random() * 4)::int
    WHEN 0 THEN 'Tilapia, Catfish, Snakehead'
    WHEN 1 THEN 'Grass Carp, Silver Carp, Indian Carp, Bighead Carp'
    WHEN 2 THEN 'Black Spotted Barb, Silver Barb, Gourami, Climbing Perch, Mosquitofish'
    WHEN 3 THEN 'Pangasius, KnifeFish, Mudfish, Goby, Freshwater Eel, Gold Fish'
    ELSE 'Tilapia, Catfish, Snakehead, Grass Carp, Silver Carp, Indian Carp, Bighead Carp'
  END),
('Lower Pierce Fishing Platform', 1.3720, 103.8271, 'Freshwater',
  CASE (random() * 4)::int
    WHEN 0 THEN 'Tilapia, Catfish, Snakehead'
    WHEN 1 THEN 'Grass Carp, Silver Carp, Indian Carp, Bighead Carp'
    WHEN 2 THEN 'Black Spotted Barb, Silver Barb, Gourami, Climbing Perch, Mosquitofish'
    WHEN 3 THEN 'Pangasius, KnifeFish, Mudfish, Goby, Freshwater Eel, Gold Fish'
    ELSE 'Tilapia, Catfish, Snakehead, Grass Carp, Silver Carp, Indian Carp, Bighead Carp'
  END),

-- Punggol Waterway Spots
('Punggol Point Jetty', 1.4042, 103.9077, 'Brackish',
  CASE (random() * 4)::int
    WHEN 0 THEN 'Bangus, Fourfinger Threadfin, Indo-Pacific Tarpon'
    WHEN 1 THEN 'Mullet, Tenpounder, Snakehead, Silver Perch'
    WHEN 2 THEN 'Catfish, Climbing Perch, Mudfish, Goby, Gourami'
    WHEN 3 THEN 'Tilapia, Silver Barb, Scat Fish, Perch, Pangasius, KnifeFish'
    ELSE 'Freshwater Eel, Glass Perchlet, Mosquitofish, Long-Snouted Pipefish, Green Spotted Puffer, Janitor Fish, Gold Fish'
  END),
('Punggol Promenade', 1.4040, 103.9075, 'Brackish',
  CASE (random() * 4)::int
    WHEN 0 THEN 'Bangus, Fourfinger Threadfin, Indo-Pacific Tarpon'
    WHEN 1 THEN 'Mullet, Tenpounder, Snakehead, Silver Perch'
    WHEN 2 THEN 'Catfish, Climbing Perch, Mudfish, Goby, Gourami'
    WHEN 3 THEN 'Tilapia, Silver Barb, Scat Fish, Perch, Pangasius, KnifeFish'
    ELSE 'Freshwater Eel, Glass Perchlet, Mosquitofish, Long-Snouted Pipefish, Green Spotted Puffer, Janitor Fish, Gold Fish'
  END),

-- Marina Reservoir Spots
('Marina Bay Fishing Area', 1.2858, 103.8576, 'Saltwater',
  CASE (random() * 4)::int
    WHEN 0 THEN 'Bangus, Fourfinger Threadfin, Tenpounder'
    WHEN 1 THEN 'Mullet, Indo-Pacific Tarpon, Scat Fish, Silver Perch'
    WHEN 2 THEN 'Long-Snouted Pipefish, Green Spotted Puffer, Glass Perchlet, Goby, Perch'
    WHEN 3 THEN 'Bangus, Mullet, Tenpounder, Indo-Pacific Tarpon, Scat Fish, Silver Perch'
    ELSE 'Fourfinger Threadfin, Long-Snouted Pipefish, Green Spotted Puffer, Glass Perchlet, Goby, Perch, Mullet'
  END),
('Marina Barrage', 1.2856, 103.8574, 'Saltwater',
  CASE (random() * 4)::int
    WHEN 0 THEN 'Bangus, Fourfinger Threadfin, Tenpounder'
    WHEN 1 THEN 'Mullet, Indo-Pacific Tarpon, Scat Fish, Silver Perch'
    WHEN 2 THEN 'Long-Snouted Pipefish, Green Spotted Puffer, Glass Perchlet, Goby, Perch'
    WHEN 3 THEN 'Bangus, Mullet, Tenpounder, Indo-Pacific Tarpon, Scat Fish, Silver Perch'
    ELSE 'Fourfinger Threadfin, Long-Snouted Pipefish, Green Spotted Puffer, Glass Perchlet, Goby, Perch, Mullet'
  END),

-- Jurong Lake Spots
('Chinese Garden Fishing Deck', 1.3398, 103.7273, 'Freshwater',
  CASE (random() * 4)::int
    WHEN 0 THEN 'Tilapia, Catfish, Snakehead'
    WHEN 1 THEN 'Grass Carp, Silver Carp, Indian Carp, Bighead Carp'
    WHEN 2 THEN 'Black Spotted Barb, Silver Barb, Gourami, Climbing Perch, Mosquitofish'
    WHEN 3 THEN 'Pangasius, KnifeFish, Mudfish, Goby, Freshwater Eel, Gold Fish'
    ELSE 'Tilapia, Catfish, Snakehead, Grass Carp, Silver Carp, Indian Carp, Bighead Carp'
  END),
('Jurong Lake Fishing Platform', 1.3396, 103.7271, 'Freshwater',
  CASE (random() * 4)::int
    WHEN 0 THEN 'Tilapia, Catfish, Snakehead'
    WHEN 1 THEN 'Grass Carp, Silver Carp, Indian Carp, Bighead Carp'
    WHEN 2 THEN 'Black Spotted Barb, Silver Barb, Gourami, Climbing Perch, Mosquitofish'
    WHEN 3 THEN 'Pangasius, KnifeFish, Mudfish, Goby, Freshwater Eel, Gold Fish'
    ELSE 'Tilapia, Catfish, Snakehead, Grass Carp, Silver Carp, Indian Carp, Bighead Carp'
  END),

-- Pasir Ris Park Spots
('Pasir Ris Fishing Pond', 1.3722, 103.9494, 'Freshwater',
  CASE (random() * 4)::int
    WHEN 0 THEN 'Tilapia, Catfish, Snakehead'
    WHEN 1 THEN 'Grass Carp, Silver Carp, Indian Carp, Bighead Carp'
    WHEN 2 THEN 'Black Spotted Barb, Silver Barb, Gourami, Climbing Perch, Mosquitofish'
    WHEN 3 THEN 'Pangasius, KnifeFish, Mudfish, Goby, Freshwater Eel, Gold Fish'
    ELSE 'Tilapia, Catfish, Snakehead, Grass Carp, Silver Carp, Indian Carp, Bighead Carp'
  END),
('Pasir Ris Beach', 1.3720, 103.9492, 'Saltwater',
  CASE (random() * 4)::int
    WHEN 0 THEN 'Bangus, Fourfinger Threadfin, Tenpounder'
    WHEN 1 THEN 'Mullet, Indo-Pacific Tarpon, Scat Fish, Silver Perch'
    WHEN 2 THEN 'Long-Snouted Pipefish, Green Spotted Puffer, Glass Perchlet, Goby, Perch'
    WHEN 3 THEN 'Bangus, Mullet, Tenpounder, Indo-Pacific Tarpon, Scat Fish, Silver Perch'
    ELSE 'Fourfinger Threadfin, Long-Snouted Pipefish, Green Spotted Puffer, Glass Perchlet, Goby, Perch, Mullet'
  END),

-- East Coast Park Spots
('East Coast Area A', 1.3013, 103.9132, 'Saltwater',
  CASE (random() * 4)::int
    WHEN 0 THEN 'Bangus, Fourfinger Threadfin, Tenpounder'
    WHEN 1 THEN 'Mullet, Indo-Pacific Tarpon, Scat Fish, Silver Perch'
    WHEN 2 THEN 'Long-Snouted Pipefish, Green Spotted Puffer, Glass Perchlet, Goby, Perch'
    WHEN 3 THEN 'Bangus, Mullet, Tenpounder, Indo-Pacific Tarpon, Scat Fish, Silver Perch'
    ELSE 'Fourfinger Threadfin, Long-Snouted Pipefish, Green Spotted Puffer, Glass Perchlet, Goby, Perch, Mullet'
  END),
('East Coast Jetty', 1.3011, 103.9130, 'Saltwater',
  CASE (random() * 4)::int
    WHEN 0 THEN 'Bangus, Fourfinger Threadfin, Tenpounder'
    WHEN 1 THEN 'Mullet, Indo-Pacific Tarpon, Scat Fish, Silver Perch'
    WHEN 2 THEN 'Long-Snouted Pipefish, Green Spotted Puffer, Glass Perchlet, Goby, Perch'
    WHEN 3 THEN 'Bangus, Mullet, Tenpounder, Indo-Pacific Tarpon, Scat Fish, Silver Perch'
    ELSE 'Fourfinger Threadfin, Long-Snouted Pipefish, Green Spotted Puffer, Glass Perchlet, Goby, Perch, Mullet'
  END);

-- Sample Reviews
INSERT INTO baitmate.review (content, user_id, post_id) VALUES
('Great spot for beginners, lots of fish and good facilities', 1, 1),
('Good facilities but can be crowded on weekends', 2, 2),
('Perfect for evening fishing, peaceful environment', 3, 3),
('Nice location but limited parking available', 4, 4),
('Excellent fishing spot with great variety of fish', 5, 5);

-- Sample Posts
INSERT INTO baitmate.post (title, content, poststatus, posttime, likecount, savedcount, accuracyscore, userid, location) VALUES
('First Fishing Trip!', 'Had an amazing time fishing at Bedok Reservoir. Caught several beautiful fish!', 'ACTIVE', '2024-01-01 09:00:00'::timestamp, 25, 10, 4.5, 1, 1),
('Fishing Tips for Beginners', 'Here are some essential tips for those starting out in fishing...', 'ACTIVE', '2024-01-03 14:30:00'::timestamp, 42, 15, 4.8, 2, 2),
('Best Fishing Spots in Singapore', 'Sharing my favorite fishing locations around Singapore...', 'ACTIVE', '2024-01-05 11:15:00'::timestamp, 38, 20, 4.6, 3, 1),
('Weekend Fishing Adventure', 'Spent a great weekend fishing at Lower Pierce...', 'ACTIVE', '2024-01-10 08:45:00'::timestamp, 30, 12, 4.3, 4, 2),
('Catch of the Day!', 'Look at this amazing fish I caught at Punggol...', 'ACTIVE', '2024-01-15 16:20:00'::timestamp, 45, 25, 4.9, 5, 3),
('Fishing Equipment Review', 'Reviewing my new fishing rod and other gear...', 'ACTIVE', '2024-01-20 13:10:00'::timestamp, 35, 18, 4.4, 1, 4),
('Early Morning Fishing', 'The sunrise at Marina Bay was beautiful...', 'ACTIVE', '2024-01-22 05:30:00'::timestamp, 28, 14, 4.2, 2, 5),
('Family Fishing Day', 'Took the kids fishing at Jurong Lake...', 'ACTIVE', '2024-01-24 11:00:00'::timestamp, 32, 16, 4.5, 3, 6),
('Night Fishing Experience', 'Tried night fishing at East Coast Park...', 'ACTIVE', '2024-01-26 20:45:00'::timestamp, 40, 22, 4.7, 4, 7),
('Monthly Fishing Roundup', 'Highlights from this month''s fishing adventures...', 'ACTIVE', '2024-01-29 16:00:00'::timestamp, 38, 19, 4.7, 2, 1),
('Rainy Day Fishing', 'Despite the weather, had some amazing catches at Bedok!', 'ACTIVE', '2024-01-30 14:20:00'::timestamp, 28, 14, 4.3, 3, 1),
('New Fishing Technique', 'Tried a new fishing technique at Lower Pierce today...', 'ACTIVE', '2024-01-31 10:15:00'::timestamp, 35, 18, 4.6, 4, 2),
('Sunset Fishing Session', 'Beautiful sunset while fishing at Punggol Waterway...', 'ACTIVE', '2024-02-01 18:30:00'::timestamp, 42, 20, 4.8, 5, 3),
('Group Fishing Event', 'Organized a group fishing event at Marina Bay...', 'ACTIVE', '2024-02-02 09:45:00'::timestamp, 50, 25, 4.9, 1, 4),
('Fishing Competition Results', 'Great turnout at today''s competition in Jurong Lake...', 'ACTIVE', '2024-02-03 17:00:00'::timestamp, 45, 22, 4.7, 2, 5),
('Peaceful Morning Catch', 'Early morning fishing at East Coast Park was so peaceful...', 'ACTIVE', '2024-02-04 06:30:00'::timestamp, 33, 16, 4.4, 3, 6),
('Weekend Fishing Success', 'Had a successful fishing trip at Bedok Reservoir...', 'ACTIVE', '2024-02-05 15:20:00'::timestamp, 38, 19, 4.6, 4, 7),
('Fish Species Guide', 'Guide to common fish species found in Singapore waters...', 'ACTIVE', '2024-02-06 11:45:00'::timestamp, 55, 30, 4.9, 5, 1),
('Fishing Weather Tips', 'Best weather conditions for fishing in Singapore...', 'ACTIVE', '2024-02-07 13:30:00'::timestamp, 40, 21, 4.5, 1, 2),
('Local Fishing Community', 'Great meetup with fellow anglers at Punggol today...', 'ACTIVE', '2024-02-08 19:15:00'::timestamp, 48, 24, 4.8, 2, 3);

-- Sample Comments
INSERT INTO baitmate.comment (comment, time, like_count, post_id, user_id) VALUES
('Amazing catch!', '2024-01-15 10:30:00'::timestamp, 10, 1, 2),
('What bait did you use?', '2024-01-20 15:00:00'::timestamp, 5, 3, 1),
('Love the view!', '2024-01-25 07:00:00'::timestamp, 8, 5, 4),
('Great tips, thanks for sharing!', '2024-01-28 09:45:00'::timestamp, 15, 7, 3),
('Keep up the good work!', '2024-01-30 14:20:00'::timestamp, 7, 9, 5),
('Impressive!', '2024-02-02 12:00:00'::timestamp, 12, 11, 5),
('Beautiful photo!', '2024-02-02 16:15:00'::timestamp, 9, 13, 3);

-- Sample System Reports
INSERT INTO baitmate.system_report (
    report_type, report_summary, generation_date, generated_by, period_start, period_end,
    system_uptime, server_response_time, active_users, new_catch_records, hotspot_visits, error_count,
    report_file_path, is_archived
) VALUES
('PERFORMANCE', 'System performance report for January 2024', '2024-01-10 09:00:00'::timestamp, 1, '2024-01-01', '2024-01-31',
 99.9, 0.5, 150, 45, 320, 2, '/reports/performance_jan2024.pdf', false),
('USER_ACTIVITY', 'User activity analysis for January 2024', '2024-01-12 14:00:00'::timestamp, 1, '2024-01-01', '2024-01-31',
 99.8, 0.6, 180, 52, 380, 1, '/reports/user_activity_jan2024.pdf', false),
('ERROR_LOG', 'System error analysis for January 2024', '2024-01-14 16:00:00'::timestamp, 1, '2024-01-01', '2024-01-31',
 99.7, 0.7, 165, 48, 350, 5, '/reports/error_log_jan2024.pdf', false),
('FISHING_DATA', 'Fishing activity analysis for January 2024', '2024-01-20 10:30:00'::timestamp, 1, '2024-01-01', '2024-01-31',
 99.9, 0.4, 200, 60, 400, 0, '/reports/fishing_data_jan2024.pdf', false),
('PERFORMANCE', 'System performance report for February 2024', '2024-02-01 13:45:00'::timestamp, 1, '2024-02-01', '2024-02-29',
 99.8, 0.5, 220, 65, 420, 1, '/reports/performance_feb2024.pdf', false);

-- Reset catch_record table and its sequence
TRUNCATE TABLE baitmate.catch_record RESTART IDENTITY CASCADE;

-- Sample Catch Records (200 records with random fish IDs between 1-31)
INSERT INTO baitmate.catch_record (time, length, weight, latitude, longitude, remark, fish_id, user_id, spot_id, location_id)
SELECT 
    timestamp '2024-01-01 00:00:00' + 
    (random() * (timestamp '2024-02-01 00:00:00' - timestamp '2024-01-01 00:00:00')) as time,
    20 + (random() * 30)::numeric(10,2) as length,  -- Length between 20-50 cm
    0.5 + (random() * 4.5)::numeric(10,2) as weight,  -- Weight between 0.5-5.0 kg
    1.3 + (random() * 0.1) as latitude,  -- Around Singapore
    103.8 + (random() * 0.1) as longitude,
    CASE (random() * 3)::int
        WHEN 0 THEN 'Great catch!'
        WHEN 1 THEN 'Personal best!'
        WHEN 2 THEN 'Nice fishing day'
        ELSE 'Fun experience'
    END as remark,
    (random() * 30 + 1)::int as fish_id,
    (random() * 4 + 1)::int as user_id,
    (random() * 13 + 1)::int as spot_id,  -- Changed from 19 to 13 since we have 14 spots (1-14)
    (random() * 6 + 1)::int as location_id
FROM generate_series(1, 200);

-- Reset fish_in_fishing_location table and its sequence
TRUNCATE TABLE baitmate.fish_in_fishing_location RESTART IDENTITY CASCADE;

-- Sample Fish in Fishing Location data
INSERT INTO baitmate.fish_in_fishing_location (fish_id, location_id)
SELECT 
    (random() * 30 + 1)::int as fish_id,
    (random() * 6 + 1)::int as location_id
FROM generate_series(1, 50);  -- Generate 50 random fish-location associations

-- Sample Post Likes
INSERT INTO baitmate.post_likes (post_id, user_id) VALUES
(1, 2), (1, 3), (1, 4),  -- First post liked by users 2, 3, 4
(2, 1), (2, 3), (2, 5),  -- Second post liked by users 1, 3, 5
(3, 1), (3, 2), (3, 4),  -- Third post liked by users 1, 2, 4
(4, 2), (4, 3), (4, 5),  -- Fourth post liked by users 2, 3, 5
(5, 1), (5, 3), (5, 4),  -- Fifth post liked by users 1, 3, 4
(6, 2), (6, 4), (6, 5),  -- Sixth post liked by users 2, 4, 5
(7, 1), (7, 3), (7, 5),  -- Seventh post liked by users 1, 3, 5
(8, 2), (8, 3), (8, 4),  -- Eighth post liked by users 2, 3, 4
(9, 1), (9, 4), (9, 5),  -- Ninth post liked by users 1, 4, 5
(10, 2), (10, 3), (10, 5); -- Tenth post liked by users 2, 3, 5

-- Sample Post Saves
INSERT INTO baitmate.post_saves (post_id, user_id) VALUES
(1, 3), (1, 5),  -- First post saved by users 3, 5
(2, 1), (2, 4),  -- Second post saved by users 1, 4
(3, 2), (3, 5),  -- Third post saved by users 2, 5
(4, 1), (4, 3),  -- Fourth post saved by users 1, 3
(5, 2), (5, 4),  -- Fifth post saved by users 2, 4
(6, 3), (6, 5),  -- Sixth post saved by users 3, 5
(7, 1), (7, 4),  -- Seventh post saved by users 1, 4
(8, 2), (8, 5),  -- Eighth post saved by users 2, 5
(9, 1), (9, 3),  -- Ninth post saved by users 1, 3
(10, 2), (10, 4); -- Tenth post saved by users 2, 4

-- Sample Images
INSERT INTO baitmate.image (image_url, post_id) VALUES
('https://storage.baitmate.com/images/catch1.jpg', 1),
('https://storage.baitmate.com/images/catch2.jpg', 2),
('https://storage.baitmate.com/images/catch3.jpg', 3),
('https://storage.baitmate.com/images/catch4.jpg', 4),
('https://storage.baitmate.com/images/catch5.jpg', 5),
('https://storage.baitmate.com/images/catch6.jpg', 6),
('https://storage.baitmate.com/images/catch7.jpg', 7);
