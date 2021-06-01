function readFile(input) {
    if (input.files && input.files[0]) {
      var reader = new FileReader();
   
      reader.onload = function(e) {

        var extensionFichero= input.files[0].name.slice(-3);
         
        var htmlPreview ="";

        console.log(extensionFichero);
        if(extensionFichero == 'png' || extensionFichero == 'jpg' || extensionFichero == 'jpeg'){

            htmlPreview =
          '<img width="200" src="' + e.target.result + '" />' +
          '<p>' + input.files[0].name + '</p>';

        }
        if(extensionFichero == 'mp3'){
            htmlPreview =
            '<audio controls src="'+e.target.result+'" type="audio/mpeg"></audio>' +
            '<p>' + input.files[0].name + '</p>';
        }

        if(extensionFichero == 'mp4'){

            htmlPreview =
            '<video controls src="'+e.target.result+'" type="video/mp4" width="200" height="200px"></video>' +
            '<p>' + input.files[0].name + '</p>';
        }
        
          
        var wrapperZone = $(input).parent();
        var previewZone = $(input).parent().parent().find('.preview-zone');
        var boxZone = $(input).parent().parent().find('.preview-zone').find('.box').find('.box-body');
   
        wrapperZone.removeClass('dragover');
        previewZone.removeClass('d-none');
        boxZone.empty();
        boxZone.append(htmlPreview);
      };
   
      reader.readAsDataURL(input.files[0]);
    }
  }
   
  function reset(e) {
    e.wrap('<form>').closest('form').get(0).reset();
    e.unwrap();
  }
   
  $(".dropzone").change(function() {
    readFile(this);
  });
   
  $('.dropzone-wrapper').on('dragover', function(e) {
    e.preventDefault();
    e.stopPropagation();
    $(this).addClass('dragover');
  });
   
  $('.dropzone-wrapper').on('dragleave', function(e) {
    e.preventDefault();
    e.stopPropagation();
    $(this).removeClass('dragover');
  });
   
  $('.remove-preview').on('click', function() {
    var boxZone = $(this).parents('.preview-zone').find('.box-body');
    var previewZone = $(this).parents('.preview-zone');
    var dropzone = $(this).parents('.form-group').find('.dropzone');
    boxZone.empty();
    previewZone.addClass('d-none');
    reset(dropzone);
  });
  