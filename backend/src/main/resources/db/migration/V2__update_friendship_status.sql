-- Update all existing friendships to ACCEPTED status
UPDATE friendships SET status = 'ACCEPTED' WHERE status = 'PENDING';

-- Add a check constraint to ensure status is not null
ALTER TABLE friendships ADD CONSTRAINT friendships_status_not_null CHECK (status IS NOT NULL); 