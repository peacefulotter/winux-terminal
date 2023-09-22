package controllers

import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.api.libs.json.Writes
import play.api.mvc.Codec

trait WritableImplicits {
	implicit def jsonWritable[A](implicit writes: Writes[A], codec: Codec): Writeable[A] = {
		implicit val contentType: ContentTypeOf[A] = ContentTypeOf[A](Some(ContentTypes.JSON))
		val transform = Writeable.writeableOf_JsValue.transform compose writes.writes
		Writeable(transform)
	}
}

object WritableImplicits extends WritableImplicits
