package app.moc.shared.data.business

import app.moc.shared.data.api.BusinessService

interface BusinessRepository

class DefaultBusinessRepository(
        private val service: BusinessService
) : BusinessRepository