import { useEffect, useState } from "react";
import BookModel from '../../Models/BookModel';
import { SpinnerLoading } from "../Utils/SpinnerLoading";
import { SearchBook } from "./components/SearchBook";
import { Pagination } from "../Utils/Pagination";
import { Link } from "react-router-dom";
import CatalogModel from "../../Models/CatalogModel";


export const SearchBooksPage = () => {

    const [books, setBooks] = useState<BookModel[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [httpError, setHttpError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [booksPerPage] = useState(5);
    const [totalAmountOfBooks, setTotalBooks] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [categorySelection, setCategorySelection] = useState('All');
    const [search, setSearch] = useState('');
    const [searchUrl, setSearchUrl] = useState('');

    useEffect(() => {

        const fetchBooks = async () => {
            
            const baseUrl: string = `${process.env.REACT_APP_API}/books/catalog`;

            let url: string = '';

            if(searchUrl === '') {
                url = `${baseUrl}?page=${currentPage - 1}&size=${booksPerPage}`;
            } else {
                let searchWithPage = searchUrl.replace('<pageNumber>', `${currentPage - 1}`);
                url = baseUrl + searchWithPage;
            }

            const reqOptions = {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            };

            const response = await fetch(url, reqOptions);

            if(!response.ok) {
                throw new Error('Something Went Wrong')
            }

            const resJson: CatalogModel<BookModel> = await response.json();

            setTotalBooks(resJson.totalItems);
            setTotalPages(resJson.totalPages);

            // const loadedBooks: BookModel[] = resData;

            // for(const key in resData) {

            //     loadedBooks.push({
            //         id: resData[key].id,
            //         title: resData[key].title,
            //         author: resData[key].author,
            //         description: resData[key].description,
            //         copies: resData[key].copies,
            //         copiesAvailable: resData[key].copiesAvailable,
            //         category: resData[key].category,
            //         img: resData[key].img
            //     });
            // }

            setBooks(resJson.items);
            setIsLoading(false);
        };
        fetchBooks().catch((error: any) => {
            setIsLoading(false);
            setHttpError(error.message);
        });

        window.scrollTo(0, 0);
    }, [currentPage, searchUrl]);

    if(isLoading) {

        return (
           <SpinnerLoading/>
        );
    }

    if(httpError) {
        return (
            <div className='container m-5'>
                <p>{httpError}</p>
            </div>
        );
    }

    const searchHandleChange = () => {
        
        setCurrentPage(1);
        if(search !== '') {
            setSearchUrl(`/search?title=${search}&page=<pageNumber>&size=${booksPerPage}`);
            setCategorySelection('All');
        }
    }

    const categoryField = (value: string) => {

        setCurrentPage(1);
        const map: { [key: string]: string} = {'fe': "Front-end", "be": "Back-end", 'data': "Data", 'devops': "DevOps"};
        if(
            value.toLowerCase() === 'fe' ||
            value.toLowerCase() === 'be' ||
            value.toLowerCase() === 'data' ||
            value.toLowerCase() === 'devops'
        ) {
            setCategorySelection(map[value.toLowerCase()]);
            setSearchUrl(`/type?cat=${value}&page=<pageNumber>&size=${booksPerPage}`);
        } else {
            setCategorySelection('All');
            setSearchUrl('');
        }
    }

    const indexOfLastBook: number = currentPage * booksPerPage;
    const indexOfFirstBook: number = indexOfLastBook - booksPerPage;
    let lastItem = booksPerPage * currentPage <= totalAmountOfBooks ? booksPerPage * currentPage : totalAmountOfBooks;

    const paginate = (pageNumber: number) => setCurrentPage(pageNumber);

    return (
        <div>
            <div className="container">
                <div>
                    <div className="row mt-5">
                        <div className="col-6">
                            <div className="d-flex">
                                <input className="form-control me-2" type="search" placeholder="Search" 
                                aria-labelledby="Search"
                                onChange={e => setSearch(e.target.value)}
                                 />
                                <button className="btn btn-outline-success" onClick={() => searchHandleChange()}>
                                    Search
                                </button>
                            </div>
                        </div>
                        <div className="col-4">
                            <div className="dropdown">
                                <button className="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton1"
                                    data-bs-toggle='dropdown' aria-expanded='false'>{categorySelection}</button>
                                <ul className="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                                    <li onClick={() => categoryField('All')}>
                                        <a href="#" className="dropdown-item">
                                            All
                                        </a>
                                    </li>
                                    <li onClick={() => categoryField('fe')}>
                                        <a href="#" className="dropdown-item">
                                            Front End
                                        </a>
                                    </li>
                                    <li onClick={() => categoryField('be')}>
                                        <a href="#" className="dropdown-item">
                                            Back End
                                        </a>
                                    </li>
                                    <li onClick={() => categoryField('data')}>
                                        <a href="#" className="dropdown-item">
                                            Data
                                        </a>
                                    </li>
                                    <li onClick={() => categoryField('devops')}>
                                        <a href="#" className="dropdown-item">
                                            DevOps
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    {totalAmountOfBooks > 0 ?

                        <>
                        <div className="mt-3">
                            <h5>Number of results: {totalAmountOfBooks}</h5>
                        </div>
                        <p>
                            {indexOfFirstBook + 1} to {lastItem} of {totalAmountOfBooks} items:
                        </p>
                        {books.map(book => (
                            <SearchBook book = {book}/>
                        ))}
                        </>

                        :
                        
                        <div className="m-5">
                            <h3>Can't find what you're looking for?</h3>
                            <Link to="/messages" type="button" className="btn main-color btn-md px-4 me-md-2 fw-bold text-white">
                                Library Services
                            </Link>
                        </div>
                    }
                    {totalPages > 1 && 
                        <Pagination currentPage={currentPage} totalPages={totalPages} paginate={paginate}/>
                    }
                </div>
            </div>
        </div>
    );
}