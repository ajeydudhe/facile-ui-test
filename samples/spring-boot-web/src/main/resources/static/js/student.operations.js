function loadAllStudentDetails() {
  
  console.log('Loading student details...');
  
  $.getJSON("/students")
    .done(function(students) {
    
    console.log('Received students: %s', JSON.stringify(students));
    
    displayStudents(students);
    
  }).fail(function(error) {
    
    console.error('### Error: %s', JSON.stringify(error));
  });
}

function loadSingleStudentDetails() {
  
  var studentId = window.location.pathname.substring(window.location.pathname.lastIndexOf("/") + 1);
  
  console.log('Loading student details for %s...', studentId);
  
  $.getJSON('/students/' + studentId)
   .done(function(student) {
    
    console.log('Received student: %s', JSON.stringify(student));
    
    displayStudentDetails(student);
    
  }).fail(function(error) {
    
    console.error('### Error: %s', JSON.stringify(error));
    
    $('#errorMessage').text(error.responseText);
  });
}

function createStudent() {
  
  studentCRUD('POST', getStudent(), 'Create');
}

function updateStudent() {
  
  studentCRUD('PUT', getStudent(), 'Update');
}

function deleteStudent() {
  
  studentCRUD('DELETE', getStudent(), 'Delete');
}

function studentCRUD(httpMethod, payload, operationContext) {
  
  console.log('%s student: %s', operationContext, JSON.stringify(payload));

  $.ajax({
    
    url: '/students',
    type: httpMethod,
    data: JSON.stringify(payload),
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    success: function(student, status, jqXHR) {
      
      console.log('%sd student: %s', operationContext, JSON.stringify(student));

      // Special case for testing headers for delete operation only
      var deleteHeaderValue = jqXHR.getResponseHeader('X-MY-DELETE-RESPONSE-HEADER');
      console.log('deleteHeaderValue: %s', deleteHeaderValue);

      if(operationContext === 'Delete' && deleteHeaderValue !== 'ThisIsDeleteResponse') {
        
        throw "deleteHeaderValue should be 'ThisIsDeleteResponse' but defined as '" + deleteHeaderValue + "' in response headers.";
      }
      
      displayStudentDetails(student);
    },
    error: function(error) {
      
      console.error('### Error (%s): %s', operationContext, JSON.stringify(error));
      
      $('#errorMessage').text(error.responseText);
    }
  });   
}

function displayStudents(students) {
  
  var tableRows = document.createDocumentFragment();
  
  var tr = document.createElement("tr");
  tr.appendChild(createElementWithContent("th", "", "<b>ID</b>"));
  tr.appendChild(createElementWithContent("th", "", "<b>First Name</b>"));
  tr.appendChild(createElementWithContent("th", "", "<b>Last Name</b>"));
  tr.appendChild(createElementWithContent("th", "", "<b>Age</b>"));
  
  tableRows.appendChild(tr);
  
  $.each(students, function(index, student) {
    
    tr = document.createElement("tr");

    var studentId = "<a id='studentId' href='/students/" + student.studentId + "' target='_blank'>" + student.studentId + "</a>";
    tr.appendChild(createElementWithContent("td", "", studentId));
    tr.appendChild(createElementWithContent("td", "firstName", student.firstName));
    tr.appendChild(createElementWithContent("td", "lastName", student.lastName));
    tr.appendChild(createElementWithContent("td", "age", student.age));
    
    tableRows.appendChild(tr);
  });
  
  var table = document.createElement("table");
  table.id = "allStudents";
  
  table.style.border = "1px solid black";
  
  table.appendChild(tableRows);

  document.getElementById("studentDetails").appendChild(table);  
}

function displayStudentDetails(student) {

  $('#txtStudentId').val(student.studentId);
  $('#txtFirstName').val(student.firstName);
  $('#txtLastName').val(student.lastName);
  $('#txtAge').val(student.age);
}

function createElementWithContent(tagName, elementId, content) {
  
  var element = document.createElement(tagName);
  
  element.id = elementId;
    
  element.innerHTML = content;
  
  element.style.border = "1px solid black";
  
  return element;
}

function getStudent() {
  
  return {
    
    studentId: $('#txtStudentId').val(),
    firstName: $('#txtFirstName').val(),
    lastName: $('#txtLastName').val(),
    age: $('#txtAge').val()
  }
}