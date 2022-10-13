/*
 * Copyright © 2019 admin (admin@infrastructurebuilder.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.infrastructurebuilder.data;

import org.infrastructurebuilder.exceptions.IBException;

import com.mscharhag.et.ET;
import com.mscharhag.et.ExceptionTranslator;

public class IBDataException extends IBException {
  public static ExceptionTranslator det = ET.newConfiguration().translate(Exception.class).to(IBDataException.class).done();

  private static final long serialVersionUID = 2000551302082923226L;

  public IBDataException() {
    super();
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public IBDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  /**
   * @param message
   * @param cause
   */
  public IBDataException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   */
  public IBDataException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public IBDataException(Throwable cause) {
    super(cause);
  }

}
