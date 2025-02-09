User Creation Journey -->

Steps inside the Service Layer:
Check if a user already exists (by email).
Fetch role_id from the role table based on "roles" in the request.
Hash the password before saving.
Save the user in the users table with the correct role_id.
Return a successful response.
