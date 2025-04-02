<html>
<body>
<h2>Hello World!</h2>

SpringMVC file Upload
<form name="form1", action="/manage/product/upload.do", method="post", enctype="multipart/form-data">
    <input type="file", name="upload_file" />
    <input type="submit", value="SpringMVC File Upload" />
</form>

RichText Image file Upload
<form name="form1", action="/manage/product/rich_text_image_upload.do", method="post", enctype="multipart/form-data">
    <input type="file", name="upload_file" />
    <input type="submit", value="RichText Image file Upload" />
</form>
</body>
</html>
