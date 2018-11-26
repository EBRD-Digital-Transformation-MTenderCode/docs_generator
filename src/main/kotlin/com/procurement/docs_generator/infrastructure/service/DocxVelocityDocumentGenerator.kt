package com.procurement.docs_generator.infrastructure.service

import com.procurement.docs_generator.application.service.document.DocumentGenerator
import com.procurement.docs_generator.domain.model.document.PDFDocument
import com.procurement.docs_generator.domain.model.template.Template
import com.procurement.docs_generator.exception.template.TemplateEvaluateException
import com.procurement.docs_generator.infrastructure.common.toInputStream
import fr.opensagres.xdocreport.converter.ConverterTypeTo
import fr.opensagres.xdocreport.converter.ConverterTypeVia
import fr.opensagres.xdocreport.converter.Options
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry
import fr.opensagres.xdocreport.template.TemplateEngineKind
import org.springframework.stereotype.Service

@Service
class DocxVelocityDocumentGenerator : DocumentGenerator {
    override val format: Template.Format
        get() = Template.Format.DOCX

    override val engine: Template.Engine
        get() = Template.Engine.Velocity

    override fun generate(template: Template, context: Map<String, Any>): PDFDocument {
        return try {
            val inputStream = template.body.toInputStream()
            val report = XDocReportRegistry.getRegistry()
                .loadReport(inputStream, TemplateEngineKind.Velocity, false)
            val options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF)
            PDFDocument().apply {
                report.convert(context, options, this.outputStream())
            }
        } catch (exception: Exception) {
            throw TemplateEvaluateException(
                message = "Error of evaluate template.",
                exception = exception
            )
        }
    }
}