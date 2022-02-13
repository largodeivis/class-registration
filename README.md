# Course Registration
Course Registration Java SpringBoot API.

**Prerequisites**
* Docker v20.10.10 build b485636

**Running Project**
1. Clone from GitHub.
2. Build Docker image
3. Run docker image

```
git clone git@github.com:largodeivis/class-registration.git
docker build -t class-reg .
docker run -p 8080:8080 class-reg
```

## Calling the API
### Create Student

_Post_ Request to _/students/_ with a request body.

```
localhost:8080/students
```

**Request Body**
```
{
"name": "Freddy"
}
```

![POST Students Image](img/POSTStudents.PNG)


### Create Professor

_Post_ Request to _/professors/_ with a request body.

```
localhost:8080/professors
```

**Request Body**
```
{
"name": "Scooby Doo"
}
```

![POST Professors Image](img/POSTProfessors.PNG)

### Create Courses

_Post_ Request to _/courses/_ with a request body. CourseNumbers must be unique.

```
localhost:8080/courses
```

**Request Body**
```
{
	"courseName":"Cyber Crime",
	"courseNumber":"CYB102"
}
```

![POST Courses Image](img/POSTCourses.PNG)


### Assign a Professor to a Course

_Post_ Request to _/courses/**{courseId}**/professor/**{professorId}**_. 

```
localhost:8080/courses/1/professor/1
```

![POST Assign Professor Image](img/POSTAssignProfessor.PNG)

**Response**

![Assign Professor Response Image](img/RESPONSEAssignProfessor.PNG)


### Register a Student to a Course

_Post_ Request to _/courses/**{courseId}**/student/**{studentId}**_.

```
localhost:8080/courses/1/student/1
```

![POST Register Student Image](img/POSTRegisterStudent.PNG)

**Response**

![Register Student Response Image](img/RESPONSERegisterStudent.PNG)

