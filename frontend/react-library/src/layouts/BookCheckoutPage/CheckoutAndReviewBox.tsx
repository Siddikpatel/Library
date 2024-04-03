import { Link } from "react-router-dom";
import BookModel from "../../Models/BookModel";
import { LeaveAReview } from "../Utils/LeaveAReview";

export const CheckoutAndReviewBox: React.FC<{ book: BookModel | undefined, mobile: boolean, 
    currentLoansCount: number, isAuthenticated: any, isCheckedout: boolean,
    checkoutBook: any, isReviewLeft: boolean, submitReview: any }> = (props) => {
        
        // const [didBuy, setDidBuy] = useState(false);

        // useEffect(() => {

        //     const fetchUserHistory = async () => {
    
        //         if (props.isAuthenticated && props.book) {
    
        //             const url = `${process.env.REACT_APP_API}/histories/search/wasBoughtByUser?userEmail=${props.sub.accessToken?.claims.sub}&title=${props.book.title}`;
        //             const requestOptions = {
        //                 method: "GET",
        //                 headers: {
        //                     "Content-Type": "application/json"
        //                 }
        //             };
            
        //             const historyResponse = await fetch(url, requestOptions);
            
        //             if (!historyResponse.ok) {
        //                 throw new Error("Something went wrong!");
        //             }
                    
        //             const historyResponseJson = await historyResponse.json();
    
        //             setDidBuy(Number(historyResponseJson) > 0);
        //         }
        //     };
    
        //     fetchUserHistory().catch((error: any) => {
        //         console.log("Error is:", error.message);
        //     });
    
        // }, [props.isAuthenticated, props.isCheckedout]);

    function buttonRender() {

        if(props.isAuthenticated) {

            if(!props.isCheckedout && props.currentLoansCount < 5) {
                return (
                    <button className="btn btn-success btn-lg" onClick={() => props.checkoutBook()}>Checkout</button>
                );

            } else if(props.isCheckedout) {

                return (
                    <p><b>Book Checked out. Enjoy!</b></p>
                );
            } else if(!props.isCheckedout) {

                return (
                    <p className="text-danger">Too many books checked out.</p>
                );
            }
        }

        return (
            <Link to={'/login'} className="btn btn-success btn-lg">Sign In</Link>
        );
    }

    function reviewRender() {

        if(props.isAuthenticated && !props.isReviewLeft) {
            return (
                <p>
                    <LeaveAReview submitReview={props.submitReview} />
                </p>
            );
            // if(didBuy) {
            //     return (
            //         <p>
            //             <LeaveAReview submitReview={props.submitReview} />
            //         </p>
            //     );
            // } else {
            //     return (
            //         <p className="text-success">Try the book first and come here for review!</p>
            //     );
            // }
        } else if(props.isAuthenticated && props.isReviewLeft) {
            return (
                <p><b>Thank you for your review!!</b></p>
            );
        }

        return (
            <div>
                <hr/>
                <p>Sign in to be able to leave a review.</p>
            </div>
        );
    }

    return (

        <div className={props.mobile ? 'card d-flex mt-5' : 'card col-3 container d-flex mb-5'}>
            <div className="card-body container">
                <div className="mt-3">
                    <p>
                        <b>{props.currentLoansCount}/5 </b>
                        books checked out
                    </p>
                    <hr/>
                    {props.book && props.book.copiesAvailable && props.book.copiesAvailable > 0 ?
                        <h4 className="text-success">Available</h4>
                        :
                        <h4 className="text-danger">Wait List</h4>
                    }
                    <div className="row">
                        <p className="col-6 lead">
                            <b>{props.book?.copies} </b>
                            copies
                        </p>
                        <p className="col-6 lead">
                            <b>{props.book?.copiesAvailable} </b>
                            available
                        </p>
                    </div>
                </div>
                {buttonRender()}
                <hr/>
                <p className="mt-3">
                    This number can change until placing order has been complete.
                </p>
                {reviewRender()}
            </div>
        </div>
    );
}