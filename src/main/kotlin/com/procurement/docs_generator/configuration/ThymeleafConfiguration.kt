package com.procurement.docs_generator.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.TemplateEngine
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.templateresolver.ITemplateResolver
import org.thymeleaf.templateresolver.StringTemplateResolver

@Configuration
class ThymeleafConfiguration {

    @Bean
    fun templateEngine(): TemplateEngine {
        val engine = SpringTemplateEngine()
        engine.addTemplateResolver(stringTemplateResolver())
        engine.addDialect(Java8TimeDialect())
        return engine
    }

    private fun stringTemplateResolver(): ITemplateResolver {
        val templateResolver = StringTemplateResolver()
        templateResolver.setTemplateMode("HTML")
        templateResolver.isCacheable = false
        return templateResolver
    }
}