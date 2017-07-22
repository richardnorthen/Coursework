import Data.Char

-- a) forever function
forever :: IO () -> IO ()
forever a = a >> forever a

-- b) repeatN function
repeatN :: Int -> IO () -> IO ()
repeatN 0 a = return ()
repeatN n a = a >> repeatN (n-1) a

-- c) each function
each :: [IO a] -> IO [a]
each [] = return []
each (a:as) = a >> each as

-- d) palindrome program
isPalindrome :: String -> Bool
isPalindrome s = do
  let u = parseStr s
  let v = reverse u
  compareStr(u,v)

parseStr :: String -> String
parseStr [] = []
parseStr (a:as) = do
  if (isAlpha a)
  then (toLower a) : parseStr as
  else parseStr as

compareStr :: (String, String) -> Bool
compareStr ([],[]) = True
compareStr (s:ss,t:ts) = do
  if (s==t)
  then compareStr(ss,ts)
  else False

main :: IO ()
main = do
  putStrLn $ "Please enter a phrase:"
  x <- getLine
  let y = isPalindrome x
  putStrLn $ show y
