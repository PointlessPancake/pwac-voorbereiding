-- Test data for AppUser
INSERT INTO AppUser (username, passwordhash, salt) VALUES
    ('johnDoe', '$2a$12$HteUlBrCiPGRsGP8LKwJmewKCAqeogvNDqJHHQGxYYrW38K3lUFry', '$2a$12$HteUlBrCiPGRsGP8LKwJme'), --het wachtwoord is abcdefg555
    ('janeSmith', '$2a$12$NqF70QDMWqjcs/tWfrurjeB.Yn3ynpO8OfxRVI1a/95FAGiZbe4d6', 'dummy_salt_2'), --het wachtwoord is abcdefg555!
    ('bobWilson', 'dummy_hash_3', 'dummy_salt_3');

-- Test data for ExpenseClaim
INSERT INTO ExpenseClaim (username, title, amount, description) VALUES
    ('johnDoe', 'Taxi', 45.50, 'Taxi to client meeting in city center'),
    ('johnDoe', 'Lunch', 28.75, 'Business lunch with potential client'),
    ('johnDoe', 'Train', 157.00, 'Return train ticket to London office'),

    ('janeSmith', 'Hotel', 189.99, 'One night stay at Conference Hotel'),
    ('janeSmith', 'Dinner', 62.50, 'Team dinner after project completion'),

    ('bobWilson', 'Office', 95.20, 'Office supplies for home office'),
    ('bobWilson', 'Phone', 35.00, 'Monthly phone bill reimbursement'),
    ('bobWilson', 'Parking', 15.00, 'Parking fee for client visit');