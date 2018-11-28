package com.procurement.docs_generator.infrastructure.adapter

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class WebExceptionHandler : ResponseEntityExceptionHandler()