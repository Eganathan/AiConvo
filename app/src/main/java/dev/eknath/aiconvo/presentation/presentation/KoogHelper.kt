package dev.eknath.aiconvo.presentation.presentation

import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor

class KoogHelper(private val apiKey: String) {

    private val executor by lazy { simpleGoogleAIExecutor(apiKey) }

    suspend fun generate(prompt: String, systemPrompt: String = ""): String? {
        return try {
            val agent = AIAgent(
                promptExecutor = executor,
                llmModel = GoogleModels.Gemini2_0Flash,
                systemPrompt = systemPrompt
            )
            agent.run(prompt)
        } catch (e: Exception) {
            null
        }
    }
}
