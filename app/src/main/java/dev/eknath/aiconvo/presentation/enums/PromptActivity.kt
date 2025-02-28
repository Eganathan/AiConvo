package dev.eknath.aiconvo.presentation.enums

import androidx.annotation.DrawableRes
import dev.eknath.aiconvo.R
import dev.eknath.aiconvo.presentation.presentation.ROUTES

enum class PROMPT_ACTIVITY(val prompt: String, val routes: ROUTES) {

    CHAT("", ROUTES.CHAT),
    RIDDLE(
        "Give me a riddle with answer as a json format like {\"question\":, \"answer\": } note the answer should be a single word",
        routes = ROUTES.RIDDLES
    ),
    FUNNY_JOCK("Share a funny clean jock", routes = ROUTES.HOME),
    TONGUE_TWISTER("give me a plain tongue twister", routes = ROUTES.HOME),
    MATH_CHALLENGE(
        "Give me a hard tricky math problem with numeric answer in a json format {\"question\":\$ \"answer\":\$ \"explanation\":\$}",
        routes = ROUTES.MATH_CHALLENGE
    ),
    TRUTH_OR_DARE(
        "Give me a Truth and a Dare in a json format {\"truth\":\$ \"dare\":\$}",
        routes = ROUTES.TRUTH_OR_DARE
    ),
    TECH_QUOTE(
        "Give me a single random tech related quote and author in a json format like quote=$ and author=$",
        routes = ROUTES.HOME
    ),

    /*TECH_AND_SCIENCE_NEWS(
        "Give me 5 top and open source news related to each Tech and Science and i want it in a json like {\"news\": [{type: ,headline: , summary: , link: , imageLink:]}",
        routes = ROUTES.TECH_NEWS
    ),*/
    LOGICAl_APTITUDE(
        "Can you give me a logical aptitude test with 4 options for example { \"exam\":[{\"question\":  \"options\":[{\"option_id\":  \"option\":}], \"answer_option_id\":  \"explanation\":}]}",
        routes = ROUTES.HOME
    ),
    ANALYTICAL_APPTITUDE(
        "Can you give me a analytical aptitude test with 4 options for example { \"exam\":[{\"question\":  \"options\":[{\"option_id\":  \"option\":}], \"answer\":  \"explanation\":}]}",
        routes = ROUTES.HOME
    ),
    SUMMARIZE_ARTICLE("Can you summarize this article for me ", routes = ROUTES.SUMMARIZE);


    val title: String
        get() = when (this) {
            RIDDLE -> "Riddle"
            CHAT -> "AI-Chat"
            TRUTH_OR_DARE -> "Truth or Dare"
            MATH_CHALLENGE -> "Math"
            TECH_QUOTE -> "Tech Quotes"
            FUNNY_JOCK -> "Jocks"
            TONGUE_TWISTER -> "Tongue Twister"
            LOGICAl_APTITUDE -> "Logical Aptitude"
            ANALYTICAL_APPTITUDE -> "Analytical Aptitude"
            SUMMARIZE_ARTICLE -> "Summarize"
        }

    @get:DrawableRes
    val iconRes: Int
        get() = when (this) {
            TECH_QUOTE -> R.drawable.ic_quote
            TRUTH_OR_DARE -> R.drawable.puzzle
            FUNNY_JOCK -> R.drawable.ic_laugh
            TONGUE_TWISTER -> R.drawable.ic_ferries
            RIDDLE -> R.drawable.puzzle
            MATH_CHALLENGE -> R.drawable.ic_plus_minus
            LOGICAl_APTITUDE -> R.drawable.puzzle
            ANALYTICAL_APPTITUDE -> R.drawable.puzzle
            SUMMARIZE_ARTICLE -> R.drawable.puzzle
            CHAT -> R.drawable.ic_chat
        }
}