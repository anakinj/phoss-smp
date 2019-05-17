/**
 * Copyright (C) 2014-2019 Philip Helger and contributors
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.phoss.smp.rest2;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.Nonempty;
import com.helger.http.basicauth.BasicAuthClientCredentials;
import com.helger.phoss.smp.SMPServerConfiguration;
import com.helger.phoss.smp.domain.SMPMetaManager;
import com.helger.phoss.smp.restapi.BDXR1ServerAPI;
import com.helger.phoss.smp.restapi.ISMPServerAPIDataProvider;
import com.helger.phoss.smp.restapi.SMPServerAPI;
import com.helger.photon.core.api.IAPIDescriptor;
import com.helger.photon.core.api.IAPIExecutor;
import com.helger.servlet.response.UnifiedResponse;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

public final class APIExecutorServiceMetadataDelete implements IAPIExecutor
{
  private static final Logger LOGGER = LoggerFactory.getLogger (APIExecutorServiceMetadataDelete.class);

  public void invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                         @Nonnull @Nonempty final String sPath,
                         @Nonnull final Map <String, String> aPathVariables,
                         @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                         @Nonnull final UnifiedResponse aUnifiedResponse) throws Exception
  {
    // Is the writable API disabled?
    if (SMPMetaManager.getSettings ().isRESTWritableAPIDisabled ())
    {
      LOGGER.warn ("The writable REST API is disabled. deleteServiceRegistration will not be executed.");
      aUnifiedResponse.setStatus (HttpServletResponse.SC_NOT_FOUND);
    }
    else
    {
      final String sServiceGroupID = aPathVariables.get (Rest2Filter.PARAM_SERVICE_GROUP_ID);
      final String sDocumentTypeID = aPathVariables.get (Rest2Filter.PARAM_DOCUMENT_TYPE_ID);
      final ISMPServerAPIDataProvider aDataProvider = new Rest2DataProvider (aRequestScope);
      final BasicAuthClientCredentials aBasicAuth = Rest2RequestHelper.getAuth (aRequestScope.headers ());

      switch (SMPServerConfiguration.getRESTType ())
      {
        case PEPPOL:
          new SMPServerAPI (aDataProvider).deleteServiceRegistration (sServiceGroupID, sDocumentTypeID, aBasicAuth);
          break;
        case BDXR:
          new BDXR1ServerAPI (aDataProvider).deleteServiceRegistration (sServiceGroupID, sDocumentTypeID, aBasicAuth);
          break;
        default:
          throw new UnsupportedOperationException ("Unsupported REST type specified!");
      }
      aUnifiedResponse.setStatus (HttpServletResponse.SC_OK);
    }
  }
}
