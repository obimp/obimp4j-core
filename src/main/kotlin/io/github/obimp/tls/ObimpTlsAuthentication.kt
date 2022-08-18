/*
 * OBIMP4J - Java OBIMP Lib
 * Copyright (C) 2013—2022 Alexander Krysin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.obimp.tls

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.asn1.x500.style.IETFUtils
import org.bouncycastle.tls.ServerOnlyTlsAuthentication
import org.bouncycastle.tls.TlsServerCertificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

/**
 * @author Alexander Krysin
 */
class ObimpTlsAuthentication(private val host: String) : ServerOnlyTlsAuthentication() {
    private val certificateFactory = CertificateFactory.getInstance("X.509")

    override fun notifyServerCertificate(serverCertificate: TlsServerCertificate) {
        val certificates = serverCertificate.certificate.certificateList
        val x509Certificate =
            certificateFactory.generateCertificate(certificates[0].encoded.inputStream()) as X509Certificate
        // Checks validity period
        x509Certificate.checkValidity()
        // Checks CN is equals hostname
        checkCommonName(x509Certificate)
        // Checks certificate isn't self-signed
        checkOnSelfSigned(x509Certificate)
    }

    private fun checkCommonName(x509Certificate: X509Certificate) {
        var cnIsEqualsHostname = false
        val x500Name = X500Name(x509Certificate.subjectDN.name)
        for (rdn in x500Name.getRDNs(BCStyle.CN)) {
            for (typeAndValue in rdn.typesAndValues) {
                val value = IETFUtils.valueToString(typeAndValue.value)
                if (value.equals(host, ignoreCase = true)) {
                    cnIsEqualsHostname = true
                }
            }
        }
        if (!cnIsEqualsHostname) {
            throw CertificateException("Certificate CN isn't equals host.")
        }
    }

    private fun checkOnSelfSigned(x509Certificate: X509Certificate) {
        val x500Name = X500Name(x509Certificate.issuerDN.name)
        for (rdn in x500Name.getRDNs(BCStyle.CN)) {
            for (typeAndValue in rdn.typesAndValues) {
                val value = IETFUtils.valueToString(typeAndValue.value)
                if (value.equals(host, ignoreCase = true)) {
                    throw CertificateException("Certificate is self-signed.")
                }
            }
        }
    }
}