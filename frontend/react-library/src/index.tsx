import ReactDOM from 'react-dom/client';
import './index.css';
import { App } from './App';
import { BrowserRouter } from "react-router-dom";
import { Elements } from '@stripe/react-stripe-js';
import { loadStripe } from '@stripe/stripe-js';
import { useEffect } from 'react';

const stripePromise = loadStripe('pk_test_51OkmWHSDr7PTNPBzclRqZJppTrPh0nMgOTSYIbxCyybjxsaH1AstQ3TgwwC4tPPb7EChfTY0VpP50DYv5zkeM0pF008M3wMPKM')

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

root.render(
  
  <BrowserRouter>
      <Elements stripe={stripePromise}>
        <App />
      </Elements>
  </BrowserRouter>
);


// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
