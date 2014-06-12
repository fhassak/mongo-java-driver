/*
 * Copyright (c) 2008-2014 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mongodb.operation;

import org.bson.codecs.BsonArrayCodec;
import org.bson.codecs.BsonBinaryCodec;
import org.bson.codecs.BsonBooleanCodec;
import org.bson.codecs.BsonDBPointerCodec;
import org.bson.codecs.BsonDateTimeCodec;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.BsonDoubleCodec;
import org.bson.codecs.BsonInt32Codec;
import org.bson.codecs.BsonInt64Codec;
import org.bson.codecs.BsonJavaScriptWithScopeCodec;
import org.bson.codecs.BsonMaxKeyCodec;
import org.bson.codecs.BsonMinKeyCodec;
import org.bson.codecs.BsonNullCodec;
import org.bson.codecs.BsonObjectIdCodec;
import org.bson.codecs.BsonRegularExpressionCodec;
import org.bson.codecs.BsonStringCodec;
import org.bson.codecs.BsonSymbolCodec;
import org.bson.codecs.BsonUndefinedCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;
import org.bson.codecs.TimestampCodec;
import org.bson.codecs.BsonJavaScriptCodec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.BsonArray;
import org.bson.types.BsonDocument;
import org.bson.types.BsonValue;

import java.util.HashMap;
import java.util.Map;

class CommandResultCodecProvider<P> implements CodecProvider {
    private final Map<Class<?>, Codec<?>> codecs = new HashMap<Class<?>, Codec<?>>();
    private final Decoder<P> payloadDecoder;
    private final String fieldContainingPayload;

    /**
     * Construct a new instance with the default codec for each BSON type.
     */
    public CommandResultCodecProvider(final Decoder<P> payloadDecoder, final String fieldContainingPayload) {
        this.payloadDecoder = payloadDecoder;
        this.fieldContainingPayload = fieldContainingPayload;
        addCodecs();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        if (codecs.containsKey(clazz)) {
            return (Codec<T>) codecs.get(clazz);
        }

        if (clazz == BsonArray.class) {
            return (Codec<T>) new BsonArrayCodec(registry);
        }

        if (clazz == BsonDocument.class) {
            return (Codec<T>) new CommandResultDocumentCodec<P>(registry, payloadDecoder, fieldContainingPayload);
        }

        return null;
    }

    private void addCodecs() {
        addCodec(new BsonNullCodec());
        addCodec(new BsonBinaryCodec());
        addCodec(new BsonBooleanCodec());
        addCodec(new BsonDateTimeCodec());
        addCodec(new BsonDBPointerCodec());
        addCodec(new BsonDoubleCodec());
        addCodec(new BsonInt32Codec());
        addCodec(new BsonInt64Codec());
        addCodec(new BsonMinKeyCodec());
        addCodec(new BsonMaxKeyCodec());
        addCodec(new BsonJavaScriptCodec());
        addCodec(new BsonObjectIdCodec());
        addCodec(new BsonRegularExpressionCodec());
        addCodec(new BsonStringCodec());
        addCodec(new BsonSymbolCodec());
        addCodec(new TimestampCodec());
        addCodec(new BsonUndefinedCodec());
        addCodec(new BsonJavaScriptWithScopeCodec(new BsonDocumentCodec()));
    }

    private <T extends BsonValue> void addCodec(final Codec<T> codec) {
        codecs.put(codec.getEncoderClass(), codec);
    }
}
