CREATE TABLE clip (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    channel     VARCHAR(64)  NOT NULL,
    title       VARCHAR(200) NOT NULL,
    url         TEXT         NOT NULL,
    views       INTEGER      NOT NULL DEFAULT 0 CHECK (views >= 0),
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_clip_channel ON clip (channel);
