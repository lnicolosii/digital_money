import { UserAccount, User, Transaction, Card } from "../../types";

const myInit = (method = "GET", token?: string) => {
  return {
    method,
    headers: {
      "Content-Type": "application/json",
      Authorization: token ? `Bearer ${token.replaceAll('"', "")}` : "",
    },
    mode: "cors" as RequestMode,
    cache: "default" as RequestCache,
  };
};

const myRequest = (endpoint: string, method: string, token?: string) =>
  new Request(endpoint, myInit(method, token));

const baseUrl = "http://localhost:3500";

const rejectPromise = (response?: Response): Promise<Response> =>
  Promise.reject({
    status: (response && response.status) || "00",
    statusText: (response && response.statusText) || "Ocurrió un error",
    err: true,
  });

export const login = (email: string, password: string) => {
  return fetch(myRequest(`${baseUrl}/auth/login`, "POST"), {
    body: JSON.stringify({ email, password }),
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const createAnUser = (user: User) => {
  return fetch(myRequest(`${baseUrl}/auth/register`, "POST"), {
    body: JSON.stringify(user),
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .then((data) => {
      // createAnAccount(data);
      return data;
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUser = (id: string, token: string): Promise<User> => {
  return fetch(myRequest(`${baseUrl}/users/${id}`, "GET", token))
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const updateUser = (
  id: string,
  data: any,
  token: string
): Promise<Response> => {
  return fetch(myRequest(`${baseUrl}/users/${id}`, "PATCH", token), {
    body: JSON.stringify(data),
  })
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

// TODO: remove this functionality once backend is ready
const generateCvu = (): string => {
  let cvu = "";
  for (let i = 0; i < 22; i++) {
    cvu += Math.floor(Math.random() * 10);
  }
  return cvu;
};

// TODO: remove this functionality once backend is ready
const generateAlias = (): string => {
  const words = [
    "Cuenta",
    "Personal",
    "Banco",
    "Argentina",
    "Digital",
    "Money",
    "House",
    "Bank",
    "Account",
    "Cartera",
    "Wallet",
    "Pago",
    "Pay",
    "Rapido",
    "Seguro",
  ];
  const length = 3;
  let alias = "";
  for (let i = 0; i < length; i++) {
    alias += words[Math.floor(Math.random() * words.length)];
    if (i < length - 1) {
      alias += ".";
    }
  }
  return alias;
};

// TODO: remove this functionality once backend is ready
// export const createAnAccount = (data: any): Promise<Response> => {
//   const { user, accessToken } = data;

//   const alias = generateAlias();
//   const cvu = generateCvu();
//   const account = {
//     alias,
//     cvu,
//     balance: 0,
//     name: `${user.firstName} ${user.lastName}`,
//   };

//   return fetch(
//     myRequest(`${baseUrl}/users/${user.id}/accounts`, "POST", accessToken),
//     {
//       body: JSON.stringify(account),
//     }
//   ).then((response) =>
//     response.ok ? response.json() : rejectPromise(response)
//   );
// };

export const getAccount = (id: string, token: string): Promise<UserAccount> => {
  return fetch(myRequest(`${baseUrl}/accounts/user/${id}`, "GET", token), {})
    .then((response) => {
      if (response.ok) {
        return response.json().then((account) => account);
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getAccounts = (token: string): Promise<UserAccount[]> => {
  return fetch(myRequest(`${baseUrl}/accounts`, "GET", token))
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const updateAccount = (
  id: string,
  data: any,
  token: string
): Promise<Response> => {
  return fetch(myRequest(`${baseUrl}/accounts/user/${id}`, "PATCH", token), {
    body: JSON.stringify(data),
  })
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getRecentUserActivities = (
  accountId: string,
  token: string
): Promise<Transaction[]> => {
  return fetch(
    myRequest(`${baseUrl}/accounts/${accountId}/recent-activity`, "GET", token)
  )
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};
export const getUserActivities = (
  accountId: string,
  token: string
): Promise<Transaction[]> => {
  return fetch(
    myRequest(`${baseUrl}/accounts/${accountId}/activity`, "GET", token)
  )
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUserActivity = (
  accountId: string,
  activityId: string,
  token: string
): Promise<Transaction> => {
  return fetch(
    myRequest(
      `${baseUrl}/accounts/${accountId}/activity/${activityId}`,
      "GET",
      token
    )
  )
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUserCards = (
  accountId: string,
  token: string
): Promise<Card[]> => {
  return fetch(myRequest(`${baseUrl}/cards/account/${accountId}`, "GET", token))
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUserCard = (accountId: string, cardId: string): Promise<Card> => {
  return fetch(myRequest(`${baseUrl}/cards/${cardId}/account/${accountId}`, "GET"))
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const deleteUserCard = (
  accountId: string,
  cardId: string,
  token: string
): Promise<Response> => {
  return fetch(
    myRequest(
      `${baseUrl}/cards/${cardId}/account/${accountId}`,
      "DELETE",
      token
    )
  )
    .then((response) => {
      if (response.ok) {
        return response;
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const createUserCard = (card: any, token: string): Promise<Response> => {
  return fetch(myRequest(`${baseUrl}/cards`, "POST", token), {
    body: JSON.stringify(card),
  })
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

// TODO: edit when backend is ready
export const createDepositActivity = (
  accountId: string,
  cardId: string,
  amount: number,
  token: string
) => {
  const activity = {
    amount,
    cardId,
  };

  return fetch(
    myRequest(`${baseUrl}/accounts/${accountId}/deposit`, "POST", token),
    {
      body: JSON.stringify(activity),
    }
  )
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

// TODO: remove when backend is ready
// const depositMoney = (amount: number, userId: string, token: string) => {
//   return getAccount(userId, token)
//     .then((account) => {
//       const newBalance = account.balance + amount;
//       const accountId = account.id;
//       return {
//         newBalance,
//         accountId,
//       };
//     })
//     .then(({ newBalance, accountId }) => {
//       fetch(
//         myRequest(
//           `${baseUrl}/users/${userId}/accounts/${accountId}`,
//           "PATCH",
//           token
//         ),
//         {
//           body: JSON.stringify({ balance: newBalance }),
//         }
//       )
//         .then((response) =>
//           response.ok ? response.json() : rejectPromise(response)
//         )
//         .catch((err) => {
//           console.log(err);
//           return rejectPromise(err);
//         });
//     });
// };

// TODO: edit when backend is ready
export const createTransferActivity = (
  accountId: string,
  token: string,
  origin: string,
  destination: string,
  amount: number
) => {
  const isCVU = (value: string): boolean => {
    return /^\d{22}$/.test(value);
  };

  return fetch(
    myRequest(`${baseUrl}/accounts/${accountId}/transfer`, "POST", token),
    {
      body: JSON.stringify({
        amount: amount,
        alias: isCVU(destination) ? null : destination,
        cvu: isCVU(destination) ? destination : null,
      }),
    }
  )
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

// TODO: remove when backend is ready
// const discountMoney = (amount: number, userId: string, token: string) => {
//   return getAccount(userId, token)
//     .then((account) => {
//       // amount is negavite
//       const newBalance = account.balance + amount;
//       const accountId = account.id;
//       return {
//         newBalance,
//         accountId,
//       };
//     })
//     .then(({ newBalance, accountId }) => {
//       fetch(
//         myRequest(
//           `${baseUrl}/users/${userId}/accounts/${accountId}`,
//           "PATCH",
//           token
//         ),
//         {
//           body: JSON.stringify({ balance: newBalance }),
//         }
//       )
//         .then((response) =>
//           response.ok ? response.json() : rejectPromise(response)
//         )
//         .catch((err) => {
//           console.log(err);
//           return rejectPromise(err);
//         });
//     });
// };
