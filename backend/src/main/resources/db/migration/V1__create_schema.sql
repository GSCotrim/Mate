CREATE TABLE users (
    id            UUID PRIMARY KEY,
    email         TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    name          TEXT NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE tracks (
    id          UUID PRIMARY KEY,
    title       TEXT NOT NULL,
    description TEXT,
    order_index INT NOT NULL
);

CREATE TABLE lessons (
    id               UUID PRIMARY KEY,
    track_id         UUID NOT NULL REFERENCES tracks(id),
    title            TEXT NOT NULL,
    description      TEXT,
    content_markdown TEXT,
    order_index      INT NOT NULL
);

CREATE INDEX idx_lessons_track_id ON lessons(track_id);

CREATE TABLE exercises (
    id             UUID PRIMARY KEY,
    lesson_id      UUID NOT NULL REFERENCES lessons(id),
    exercise_type  TEXT NOT NULL,
    question       TEXT NOT NULL,
    options        JSONB,
    correct_option TEXT NOT NULL,
    order_index    INT NOT NULL
);

CREATE INDEX idx_exercises_lesson_id ON exercises(lesson_id);

CREATE TABLE lesson_progress (
    id           UUID PRIMARY KEY,
    user_id      UUID NOT NULL REFERENCES users(id),
    lesson_id    UUID NOT NULL REFERENCES lessons(id),
    completed_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (user_id, lesson_id)
);

CREATE INDEX idx_lesson_progress_user_id ON lesson_progress(user_id);
CREATE INDEX idx_lesson_progress_lesson_id ON lesson_progress(lesson_id);