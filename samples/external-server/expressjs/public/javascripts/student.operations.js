function loadSingleStudentDetails() {
  
  var studentId = window.location.pathname.substring(window.location.pathname.lastIndexOf("/") + 1);
  
  console.log('Loading details for student "%s"', studentId);
  
  $.getJSON('/students/api/' + studentId)
    .done(function(student) {
    
    console.log('Received student: %s', JSON.stringify(student));
    
    displayStudentDetails(student);
    
  }).fail(function(error) {
    
    console.error('### Error: %s', JSON.stringify(error));
  });  
}

function displayStudentDetails(student) {

  $('#studentId').val(student.studentId);
  $('#firstName').val(student.firstName);
  $('#lastName').val(student.lastName);
  $('#age').val(student.age);
}
