/*** c8: start of update ***/
var res = res || {};
res.service = res.service || {};
/*** c8: start of update ***/

/*
 * File Service
 */

$(document).ready(function () {
    res.service.file = new Service_file();
});

(function () {
    // Constructor
    Service_file = function () {
        this.filepath = undefined;
        this.filename = undefined;
    };

    Service_file.prototype = {
        list: function (data, callback) {
            var companyID = res.storage.getItem("CompanyID");
            $.ajax({
                type: 'POST',
                url: res.config.baseURL + "rest/uiconfigMaintenance/fileList",
                data: {
                    'companyID': companyID,
                    'folder': data.folder
                },
                datatype: 'json',
                success: function (data) {
                    callback.success(data.FileInfoList);
                },
                error: function (data) {
                    callback.error(data.FileInfoList);
                }
            });
        },
        pictureUpload: function (data, callback) {
            var formData = new FormData();
            formData.enctype = "multipart/form-data";
            formData.append('filename', data.filename);
            formData.append('folder', data.folder);
            formData.append('form-file', data.filecontent);
            formData.append('sizeType', data.sizeType);
            $.ajax({
                type: 'POST',
                url: res.config.baseURL + "rest/uiconfigMaintenance/pictureUpload",
                data: formData,
                datatype: 'json',
                contentType: false,
                processData: false,
                cache: false,
                success: function (data) {
                    callback.success(data);
                },
                error: function (data) {
                    callback.error(data);
                }
            });
        },
        download: function (data, callback) {
            requestURL = res.config.baseURL + "rest/uiconfigMaintenance/fileDownload";
            folder = data.folder;
            filename = data.file;
            var companyID = res.storage.getItem("CompanyID");
            $.ajax({
                type: 'POST',
                url: requestURL,
                data: {
                    'folder': folder,
                    'filename': filename,
                    'companyID':companyID
                },
                datatype: 'json',
                success: function (data) {
                    callback.success(data);
                },
                error: function (data) {
                    callback.error(data);
                }
            });
        },
        remove: function (data, callback) {
            requestURL = res.config.baseURL + "rest/uiconfigMaintenance/fileRemove";
            var companyID = res.storage.getItem("CompanyID");
            $.ajax({
                type: 'POST',
                url: requestURL,
                data: {
                	'companyID': companyID,
                    folder : data.folder,
                    filename : data.file,
                    delFileList : JSON.stringify(data.delFileList),
                    confirmDel : data.confirmDel
                },
                datatype: 'json',
                success: function (data) {
                    callback.success(data);
                },
                error: function (data) {
                    callback.error(data);
                }
            });
        },
        upload: function (data, callback) {
            requestURL = res.config.baseURL + "rest/uiconfigMaintenance/fileUpload";
            var companyID = res.storage.getItem("CompanyID");
            $.ajax({
                type: 'POST',
                url: requestURL,
                data: {
                    'folder': data.folder,
                    'filename': data.title,
                    'title':data.title,
                    'title2':data.title2,
                    'expire':data.expire,
                    'desfilename': data.desfilename,
                    'overwrite': data.overwrite,
                    'contents': data.contents,
                    'picturename': data.despicturename,
                    'companyID': companyID,
                },
                datatype: 'json',
                success: function (data) {
                    callback.success(data.message);
                },
                error: function (data) {
                    callback.error(data.message);
                }
            });
        },
        pictureList: function (data, callback) {
            requestURL = res.config.baseURL + "rest/uiconfigMaintenance/pictureList";
            var companyID = res.storage.getItem("CompanyID");
            $.ajax({
                type: 'POST',
                url: requestURL,
                data: {
                    'folder': data.folder,
                    'sizeType': data.sizeType,
                    'companyID': companyID
                },
                datatype: 'json',
                success: function (data) {
                    callback.success(data);
                },
                error: function (data) {
                    callback.error(data);
                }
            });
        }
    };
})();