package com.procurement.docs_generator.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.TemplateEngine
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.templateresolver.ITemplateResolver
import org.thymeleaf.templateresolver.StringTemplateResolver

@Configuration
class ThymeleafConfiguration {

    @Bean
    fun templateEngine(): TemplateEngine {
        val templateEngine = SpringTemplateEngine()
        templateEngine.addTemplateResolver(stringTemplateResolver())
        return templateEngine
    }

    private fun stringTemplateResolver(): ITemplateResolver {
        val templateResolver = StringTemplateResolver()
        templateResolver.order = Integer.valueOf(3)
        templateResolver.setTemplateMode("HTML")
        templateResolver.isCacheable = false
        return templateResolver
    }
}