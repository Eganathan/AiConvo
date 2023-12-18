package dev.eknath.aiconvo.ui.enums

import dev.eknath.aiconvo.ui.presentation.ROUTES

enum class PROMPT_ACTIVITY(val prompt: String, val routes: ROUTES) {
    TECH_QUOTE(
        "Give me a single random tech related quote and author in a json format like quote=$ and author=$",
        routes = ROUTES.HOME
    ),
    FUNNY_JOCK("Share a funny clean jock", routes = ROUTES.HOME),
    TONGUE_TWISTER("give me a plain tongue twister", routes = ROUTES.HOME),
    RIDDLE(
        "Give me a riddle with answer as a json format like question= and answer= but the answer should be a single word",
        routes = ROUTES.RIDDLES
    ),
    MATH_PROBLEM(
        "Give me a fun math problem with numeric answer in a json format like question=\$ answer=\$ explanation=\$",
        routes = ROUTES.MATH_CHALLENGE
    ),
}