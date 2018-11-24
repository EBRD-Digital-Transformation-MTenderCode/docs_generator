package com.procurement.docs_generator.adapter

import com.procurement.docs_generator.domain.model.document.PDFDocument

interface UploadDocumentAdapter {
    fun upload(pdfDocument: PDFDocument): String
}