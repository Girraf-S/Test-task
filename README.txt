Api представляет собой микросервисное приложение.
	1-ый микросервис - n-user-service. Отвечает за регистрацию и логинизацию пользователей, 
		имеет эндпоинты для активации/деактивации пользователей.
		Сущность: User. (firstName, lastName, email, role, password, enabled)

	2-ой микросервис - n-books-service. CRUD-api для взаимодейсчтвия с книгами.
		Имеет эндпоинты для поиска книги по id, isbn.
		Сущность: Book. (isbn, genre, title, description, author, available, userId)

	3-ий микросервис - n-library-service. Введет учет книг, выбранных читателем.
		Читатель может взять, продлить, и вернуть книгу. Для каждого действия 
		требуется подтверждение библиотекаря.
		Сущность: BorrowedBook. (borrowedTime, returnTime, status)

В системе имеется 3 роли: ADMIN, LIBRARIAN, READER.


{
	n-user-service:8080         ->      localhost:8081
	n-books-service:8080        ->      localhost:8082
	n-librarian-service:8080    ->      localhost:8083
}

Сценарий.

ADMIN:
	(post) n-user-service:8080\auth\login -> {"email":"admin@gmail.com", "password":"admin"} returned: {"token":jwt} (bearer token)
	(get)  n-user-service:8080\admin\users -> returned : {[users]}
	(post) n-user-service:8080\admin\deactivate\{x}
	(get)  n-user-service:8080\admin\users?isActive=false -> returned : {[users without users(x)]}					
	(post) n-user-service:8080\admin\activate\{x} 

LIBRARIAN:
1. CRUD:
	(post) n-user-service:8080\auth\login -> {"email":"librarian@mail.com", "password":"Sub123"} returned: {"token":jwt} (bearer token)
	(get)  n-books-service:8080\api\books -> returned: {[books]}
	(get)  n-books-service:8080\api\books\{x} -> returned: {books(x)}
	(get)  n-books-service:8080\api\books\isbn\{isbn} -> returned: {books(isbn)}
	(post) n-books-service:8080\api\books -> {book}
	(put)  n-books-service:8080\api\books -> {updated_book}
	(delete) n-books-service:8080\api\books\{x} -> {[books without books(x)]}

READER:
	(get)  n-books-service:8080\api\books -> returned: {[books]}
	(get)  n-books-service:8080\api\books\{x} -> returned: {books(x)}
	(get)  n-books-service:8080\api\books\isbn\{isbn} -> returned: {books(isbn)}

READER & LIBRARIAN:

	1.(R)(post) n-library-service:8080\api\library\take\{id} --> book status change to WAITING_TO_TAKE	
	2.(L)(put)  n-library-service:8080\api\library\apply\taking\{id} --> book status change to TAKING	
	3.(R)(put)  n-library-service:8080\api\library\extend\{id} --> book status change to AWAITING_TO_EXTEND	
	4.(L)(put)  n-library-service:8080\api\library\apply\extending\{id} --> book status change to TAKING	
	5.(R)(put)  n-library-service:8080\api\library\return\{id} --> book status change to AWAITING_TO_EXTEND	
	6.(L)(put)  n-library-service:8080\api\library\apply\returning\{id} --> book status change to TAKING

	Во время 1 действия книга из таблицы books меняет статус available на false, тем самым другие читатели не могут выбрать эту же книгу
	Во время действия 6 книга из таблицы books меняет статус available на true, тем самым другие читатели могут выбрать эту же книгу

	Во время действий 2-5 включительно библиотекарь может посмотреть информацию по книгам с available=false в следуюзих эндпоинтах:
	
	(get)  n-books-service:8080\api\books\archive -> returned: {[books]}
	(get)  n-books-service:8080\api\books\archive\{x} -> returned: {books(x)}
	
	Также библиотекарь может отслеживать статус книги на эндпоинте:
	
	(get) n-library-service:8080\api\library
	(get) n-library-service:8080\api\library?status=[WAITING_TO_TAKE, TAKING, AWAITING_TO_EXTEND, AWAITING_TO_RETURN]

	Читатель может продливать и возвращать только те книги, которые он брал.

	Регистрация:
	
	(R)(post) n-user-service:8080\auth\registration\reader -> {user}
	(L)(post) n-user-service:8080\auth\registration\librarian -> {user}

READER & LIBRARIAN & ADMIN:

	(get) n-user-service:8080\account -> {info about user (id from jwt)} returned: user