/*
 * Copyright 2024-2025 NetCracker Technology Corporation
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

package org.qubership.integration.platform.sessions.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionsRuntimeException  extends RuntimeException {
    private Exception originalException;

    public SessionsRuntimeException() {
        super();
    }

    public SessionsRuntimeException(String message) {
        super(message);
    }

    public SessionsRuntimeException(String message, Exception exception) {
        super(message, exception);
        this.originalException = exception;
    }
}
