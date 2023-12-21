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
        "Give me a riddle with answer as a json format like {\"question\":, \"answer\": } note the answer should be a single word",
        routes = ROUTES.RIDDLES
    ),
    MATH_CHALLENGE(
        "Give me a hard tricky math problem with numeric answer in a json format {\"question\":\$ \"answer\":\$ \"explanation\":\$}",
        routes = ROUTES.MATH_CHALLENGE
    ),
    TECH_AND_SCIENCE_NEWS(
        "Give me 5 top and open source news related to each Tech and Science and i want it in a json like {\"news\": [{type: ,headline: , summary: , link: , imageLink:]}",
        routes = ROUTES.TECH_NEWS
    ),
    LOGICAl_APPTITUDE(
        "Can you give me a logical aptitude test with 4 options for example { \"exam\":[{\"question\":  \"options\":[{\"option_id\":  \"option\":}], \"answer\":  \"explanation\":}]}",
        routes = ROUTES.HOME
    ),
    ANALYTICAL_APPTITUDE(
        "Can you give me a analytical aptitude test with 4 options for example { \"exam\":[{\"question\":  \"options\":[{\"option_id\":  \"option\":}], \"answer\":  \"explanation\":}]}",
        routes = ROUTES.HOME
    ),
    SUMMARIZE_ARTICLE("Can you summarize this article for me ", routes = ROUTES.SUMMARIZE)
}